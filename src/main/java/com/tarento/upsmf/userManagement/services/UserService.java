package com.tarento.upsmf.userManagement.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.tarento.upsmf.userManagement.utility.KeycloakTokenRetriever;
import com.tarento.upsmf.userManagement.utility.SunbirdRCKeycloakTokenRetriever;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.*;

@Component
@PropertySource({ "classpath:application.properties" })
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private Environment env;

    @Autowired
    private KeycloakTokenRetriever keycloakTokenRetriever;

    @Autowired
    private SunbirdRCKeycloakTokenRetriever sunbirdRCKeycloakTokenRetriever;

    private static Environment environment;
    private String BASE_URL;
    private String KEYCLOAK_BASEURL;

    @PostConstruct
    public void init(){
        environment = env;
        BASE_URL = getPopertyValue("BaseURL");
        KEYCLOAK_BASEURL = getPopertyValue("keycloak_BaseURL");
    }

    public static String getPopertyValue(String property){
        return environment.getProperty(property);
    }

    private final RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders getHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String authToken = environment.getProperty("authorizationToken");
        String xAuthToken = environment.getProperty("x-authenticated-user-token");
        headers.add("Authorization","Bearer " + authToken);
        headers.add("x-authenticated-user-token",xAuthToken);
        logger.info("Getting headers...{} ", headers);
        return headers;
    }

    private HttpHeaders getHeaderForKeycloak() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JsonNode adminToken = sunbirdRCKeycloakTokenRetriever.getAdminToken();
        String authToken = adminToken.get("access_token").asText();
        headers.add("Authorization","Bearer " + authToken);
        logger.info("Getting keycloak headers...{} ", headers);
        return headers;
    }

    public ResponseEntity<JsonNode> createUser(final JsonNode body) throws URISyntaxException {
        logger.info("Creating user...{} ", body.toPrettyString());
        URI uri1 = new URI(BASE_URL + "/user/v1/sso/create");
        HttpHeaders headers = getHeader();
        HttpEntity<JsonNode> httpEntity = new HttpEntity(body, headers);
        ResponseEntity<JsonNode> result = restTemplate.postForEntity(uri1,httpEntity,JsonNode.class);
        return result;
    }

    public ResponseEntity<JsonNode> updateUser(final JsonNode body) throws URISyntaxException {
        logger.info("updateUser user...{} ", body.toPrettyString());
        RestTemplate restTemplate = new RestTemplate();
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        restTemplate.setRequestFactory(requestFactory);
        URI uri = new URI(BASE_URL + "/user/v1/update");
        HttpHeaders headers = getHeader();
        HttpEntity<JsonNode> httpEntity = new HttpEntity<>(body, headers);
        ResponseEntity<JsonNode> result = restTemplate.exchange(uri, HttpMethod.PATCH, httpEntity, JsonNode.class);
        return result;
    }

    public ResponseEntity<JsonNode> listUser(final JsonNode body) throws URISyntaxException{
        logger.info("listUser user...{} ", body.toPrettyString());
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(BASE_URL + "/user/v1/search");
        HttpHeaders headers = getHeader();
        HttpEntity httpEntity = new HttpEntity(body, headers);
        ResponseEntity<JsonNode> result = restTemplate.postForEntity(uri,httpEntity,JsonNode.class);
        return result;
    }

    public ResponseEntity<JsonNode> activateUser(final JsonNode body) throws URISyntaxException{
        logger.info("activateUser user...{} ", body.toPrettyString());
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(BASE_URL + "/user/v1/unblock");
        HttpHeaders headers = getHeader();
        HttpEntity httpEntity = new HttpEntity(body, headers);
        ResponseEntity<JsonNode> result = restTemplate.postForEntity(uri,httpEntity,JsonNode.class);
        return result;
    }

    public ResponseEntity<JsonNode> deactivateUser(final JsonNode body) throws URISyntaxException {
        logger.info("deactivateUser user...{} ", body.toPrettyString());
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(BASE_URL + "/user/v1/block");
        HttpHeaders headers = getHeader();
        HttpEntity httpEntity = new HttpEntity(body, headers);
        ResponseEntity<JsonNode> result = restTemplate.postForEntity(uri,httpEntity,JsonNode.class);
        return result;
    }

    public ResponseEntity<String> sendOTP(String phoneNumber, String name, String otp) throws URISyntaxException {
        logger.info("Sending OTP to name : {} with OTP {} ...", name, otp);
        String baseUrl = env.getProperty("otp.baseUrl");
        String username = env.getProperty("otp.userName");
        String password = env.getProperty("otp.password");
        String senderId = env.getProperty("otp.senderID");
        String message = "Hello %s, Your OTP is %s UPSMF, Lucknow";
        String message1 = String.format(message,name,otp);
        String msgType = "TXT";
        String response = "Y";
        
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("username", username)
                .queryParam("pass", password)
                .queryParam("senderid", senderId)
                .queryParam("message", message1)
                .queryParam("dest_mobileno", String.valueOf(phoneNumber))
                .queryParam("msgtype", msgType)
                .queryParam("response", response);
        
        URI uri = builder.build().encode().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        return result;
    }

    public ResponseEntity<String> generateOTP(String email) throws URISyntaxException, IOException {
        logger.info("generate OTP for user...{} ", email);
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(KEYCLOAK_BASEURL + "/user/generateOtp");
        logger.info("login user ...{} ", uri.toString());
        HttpHeaders headerForKeycloak = getHeaderForKeycloak();
        HttpEntity httpEntity = new HttpEntity(email, headerForKeycloak);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,httpEntity,String.class);
        return result;
    }

    public ResponseEntity<String> login(final JsonNode body) throws URISyntaxException, IOException {
        logger.info("login user ...{} ", body);
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(KEYCLOAK_BASEURL + "/user/login");
        logger.info("login user ...{} ", uri.toString());
        HttpHeaders headerForKeycloak = getHeaderForKeycloak();
        HttpEntity httpEntity = new HttpEntity(body, headerForKeycloak);
        ResponseEntity<String> result = restTemplate.postForEntity(uri,httpEntity,String.class);
        return result;
    }
}
