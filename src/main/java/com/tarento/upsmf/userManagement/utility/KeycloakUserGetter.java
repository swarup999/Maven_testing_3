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
public class KeycloakUserGetter {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakUserCreator.class);

    @Autowired
    private Environment env;

    private static Environment environment;

    private String KEYCLOAK_USER_BASE_URL;

    @Autowired
    private KeycloakTokenRetriever keycloakTokenRetriever;

    @PostConstruct
    public void init(){
        environment = env;
        KEYCLOAK_USER_BASE_URL = getPopertyValue("keycloak.user.baseURL");
    }

    public static String getPopertyValue(String property){
        return environment.getProperty(property);
    }
    public String findUser(final String userID) throws IOException {
        String userEndpoint = KEYCLOAK_USER_BASE_URL;
        if(userID != null ) {
            userEndpoint = userEndpoint + "/" + userID;
        }
        JsonNode adminToken = keycloakTokenRetriever.getAdminToken();
        String accessToken = adminToken.get("access_token").asText();

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(userEndpoint);

        httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        httpGet.setHeader(HttpHeaders.ACCEPT, "application/json");

        org.apache.http.HttpResponse response = httpClient.execute(httpGet);
        String responseBody = EntityUtils.toString(response.getEntity());
        return responseBody;
    }
}
