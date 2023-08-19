package com.tarento.upsmf.userManagement.utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.UUID;

@Component
@PropertySource({ "classpath:application.properties" })
public class KeycloakUserCreator {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakUserCreator.class);

    @Autowired
    private Environment env;

    @Autowired
    private KeycloakTokenRetriever keycloakTokenRetriever;

    @Autowired
    private KeycloakUserCredentialPersister keycloakUserCredentialPersister;

    private static Environment environment;

    private String KEYCLOAK_USER_BASE_URL;

    @PostConstruct
    public void init(){
        environment = env;
        KEYCLOAK_USER_BASE_URL = getPropertyValue("keycloak.user.baseURL");
    }

    public static String getPropertyValue(String property){
        return environment.getProperty(property);
    }
    public String createUser(final JsonNode request) throws IOException {
        JsonNode body = request.get("request");
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
        String userName = UUID.randomUUID().toString();
        ((ObjectNode)body).put("username",userName);
        logger.info("Request body: {}", body);
        StringEntity entity = new StringEntity(body.toPrettyString());
        httpPost.setEntity(entity);

        HttpResponse response = httpClient.execute(httpPost);
        logger.info("Response from httpClient call : {}", response);
        String responseBody = EntityUtils.toString(response.getEntity());
        logger.info("Response from keycloak Rest API call : {}", responseBody);
        if (response.getStatusLine().getStatusCode() == 201) {
            String password = ((ArrayNode)body.get("credentials")).get(0).get("value").asText();
            try {
                String strResponse = keycloakUserCredentialPersister.persistUserInfo(userName, password);
            }catch (Exception ex){
                ex.printStackTrace();
            }
            responseBody = "200";
        }
        logger.info("ResponseBody {}", responseBody);
        return responseBody;
    }
}
