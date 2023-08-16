package com.tarento.upsmf.userManagement.utility;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
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
public class KeycloakUserCreator {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakUserCreator.class);

    @Autowired
    private Environment env;

    @Autowired
    private KeycloakTokenRetriever keycloakTokenRetriever;

    private static Environment environment;

    private String KEYCLOAK_USER_BASE_URL;

    @PostConstruct
    public void init(){
        environment = env;
        KEYCLOAK_USER_BASE_URL = getPopertyValue("keycloak.user.baseURL");
    }

    public static String getPopertyValue(String property){
        return environment.getProperty(property);
    }
    public String createUser(final JsonNode body) throws IOException {
        String keycloakBaseUrl = KEYCLOAK_USER_BASE_URL;
        logger.info("keycloakBaseUrl: " ,keycloakBaseUrl);
        JsonNode adminToken = keycloakTokenRetriever.getAdminToken();
        logger.info("adminToken: {}" ,adminToken);
        String accessToken = adminToken.get("access_token").asText();
        logger.info("accessToken: {}" ,accessToken);
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(keycloakBaseUrl);

        httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        httpPost.setHeader(HttpHeaders.ACCEPT, "application/json");
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        JsonNode request = body.get("request");
        String userName = request.get("userName").asText();
        String firstName = request.get("firstName").asText();
        String lastName = request.get("lastname").asText();
        String email = request.get("email").asText();
        String password = request.get("password").asText();
        String requestBody = "{" +
            "\"enabled\": true," +
            "\"username\": " + "\"" +userName + "\"" + "," +
            "\"email\": " + "\"" + email + "\"" + "," +
            "\"firstName\": " + "\"" + firstName + "\"" + "," +
            "\"lastName\": " + "\"" + lastName + "\"" + "," +
            "\"credentials\": [{" +
                "\"type\": \"password\"," +
                "\"value\": " + "\"" + password + "\"" + "," +
                "\"temporary\": false" +
            "}]" +
        "}";
        logger.info("Request body: {}", requestBody);
        StringEntity entity = new StringEntity(requestBody);
        httpPost.setEntity(entity);

        HttpResponse response = httpClient.execute(httpPost);
        String responseBody = EntityUtils.toString(response.getEntity());
        logger.info("ResponseBody {}", responseBody);
        return responseBody;
    }
}
