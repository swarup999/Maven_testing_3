package com.tarento.upsmf.userManagement.utility;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
public class KeycloakUserCount {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakUserCount.class);

    @Autowired
    private Environment env;

    private static Environment environment;

    private String KEYCLOAK_USER_BASE_URL;

    @Autowired
    private KeycloakTokenRetriever keycloakTokenRetriever;

    @PostConstruct
    public void init(){
        environment = env;
        KEYCLOAK_USER_BASE_URL = getPropertyValue("keycloak.user.baseURL");
    }

    public static String getPropertyValue(String property){
        return environment.getProperty(property);
    }

    public String getUserCount() throws IOException {
        logger.info("userEndpoint {} " ,KEYCLOAK_USER_BASE_URL);
        JsonNode adminToken = keycloakTokenRetriever.getAdminToken();
        String accessToken = adminToken.get("access_token").asText();
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(KEYCLOAK_USER_BASE_URL+"/count");
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        httpGet.setHeader(HttpHeaders.ACCEPT, "application/json");
        org.apache.http.HttpResponse response = httpClient.execute(httpGet);
        String responseBody = EntityUtils.toString(response.getEntity());
        logger.info("ResponseBody {}", responseBody);
        return responseBody;
    }

}
