package com.tarento.upsmf.userManagement.utility;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
@PropertySource({ "classpath:application.properties" })
public class KeycloakUserUpdater {

    @Autowired
    private Environment env;

    @Autowired
    private KeycloakTokenRetriever keycloakTokenRetriever;
    private static Environment environment;
    private static final Logger logger = LoggerFactory.getLogger(KeycloakUserUpdater.class);

    private String KEYCLOAK_USER_BASE_URL;

    @PostConstruct
    public void init(){
        environment = env;
        KEYCLOAK_USER_BASE_URL = getPropertyValue("keycloak.user.baseURL");
    }

    public static String getPropertyValue(String property){
        return environment.getProperty(property);
    }

    public void updateUser() throws IOException {
        JsonNode adminToken = keycloakTokenRetriever.getAdminToken();
        logger.info("adminToken: {}" ,adminToken);
        String accessToken = adminToken.get("access_token").asText();
        HttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(KEYCLOAK_USER_BASE_URL);

        httpPut.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        httpPut.setHeader(HttpHeaders.ACCEPT, "application/json, text/plain, */*");
        httpPut.setHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");

        String requestBody = "{" +
            "\"firstName\": true," +
            "\"lastName\": {}," +
            "\"email\": \"def.ghi@yopmail.com\"," +
            "\"emailVerified\": true" +
        "}";
        logger.info("Request body: {}", requestBody);
        StringEntity entity = new StringEntity(requestBody);
        httpPut.setEntity(entity);

        HttpResponse response = httpClient.execute(httpPut);
        String responseBody = EntityUtils.toString(response.getEntity());
        logger.info("ResponseBody {}", responseBody);
        if (response.getStatusLine().getStatusCode() == 204) {
            System.out.println("User updated successfully.");
        } else {
            System.out.println("Failed to update user.");
            System.out.println("Response: " + responseBody);
        }
    }
}
