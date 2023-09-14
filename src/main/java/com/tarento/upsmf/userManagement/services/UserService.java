package com.tarento.upsmf.userManagement.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.tarento.upsmf.userManagement.model.Transaction;
import com.tarento.upsmf.userManagement.model.UserAttributeModel;
import com.tarento.upsmf.userManagement.repository.TransactionRepository;
import com.tarento.upsmf.userManagement.repository.UserAttributeRepository;
import com.tarento.upsmf.userManagement.utility.KeycloakTokenRetriever;
import com.tarento.upsmf.userManagement.utility.KeycloakUserCount;
import com.tarento.upsmf.userManagement.utility.KeycloakUserCredentialPersister;
import com.tarento.upsmf.userManagement.utility.SunbirdRCKeycloakTokenRetriever;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private KeycloakUserCredentialPersister keycloakUserCredentialPersister;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserAttributeRepository userAttributeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private KeycloakUserCount keycloakUserCount;

    private static Environment environment;
    private String BASE_URL;
    private String KEYCLOAK_BASEURL;

    private Connection connection;

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

    public String userCount() throws IOException {
        return keycloakUserCount.getUserCount();
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

    public ResponseEntity<String> paymentRedirect(Map<String, String> requestData) throws URISyntaxException, IOException {
        return paymentService.makePayment(requestData);
    }

    public String usrLogin(JsonNode body) throws IOException {
        logger.info("login user with body {}",body);
        return keycloakUserCredentialPersister.usrLogin(body);
    }

    public String usrOTP(JsonNode body) throws IOException {
        logger.info("OTP mail to user with body {}",body);
        return keycloakUserCredentialPersister.sendOTPMail(body);
    }
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> result = transactionRepository.findAll();
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<Transaction> getTransactionByUniqueRefNumber(String uniqueRefNumber) {
        Transaction transaction = transactionRepository.findByUniqueRefNumber(uniqueRefNumber);
        if (transaction == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    public List<UserAttributeModel> getUserByAttribute(String fieldName, String fieldValue) {
        return userAttributeRepository.findUserByAttribute(fieldName, fieldValue);
    }

    public List getUserListByAttribute(String fieldName, String fieldValue, int offset, int limit) throws SQLException {

        List<UserAttributeModel> userByAttribute = getUserByAttribute(fieldName, fieldValue);
        if(userByAttribute == null || userByAttribute.isEmpty()){
            logger.info("No records found.");
            return Collections.EMPTY_LIST;
        }
        logger.info("Records found {}",userByAttribute);
        List<String> collect = userByAttribute.stream().map(UserAttributeModel::getUserId).collect(Collectors.toList());

        Map<String, UserRepresentation> userRepresentationMap = getStringUserRepresentationMap(collect, offset, limit);
        if(userRepresentationMap.isEmpty()){
            logger.info("No UserRepresentation records found for {}",collect);
            return Collections.EMPTY_LIST;
        }
        return new ArrayList<>(userRepresentationMap.values());
    }

    private Map<String, UserRepresentation> getStringUserRepresentationMap(List<String> collect, int offset, int limit) throws SQLException {
        Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
        String formattedString = getFormattedStringFromCollection(collect, offset, limit);
        Map<String, UserRepresentation> userRepresentationMap = new HashMap<>();
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(formattedString);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    UserRepresentation userRepresentation = null;
                    if (userRepresentationMap.containsKey(id)) {
                        userRepresentation = userRepresentationMap.get(id);
                        userRepresentation.singleAttribute(resultSet.getString("name"), resultSet.getString("value"));
                    } else {
                        userRepresentation = new UserRepresentation();
                        userRepresentation.setId(id);
                        userRepresentation.setUsername(resultSet.getString("username"));
                        userRepresentation.setEnabled(resultSet.getBoolean("enabled"));
                        userRepresentation.setEmail(resultSet.getString("email"));
                        userRepresentation.setFirstName(resultSet.getString("first_name"));
                        userRepresentation.setLastName(resultSet.getString("last_name"));
                        userRepresentation.singleAttribute(resultSet.getString("name"), resultSet.getString("value"));
                        userRepresentationMap.put(id, userRepresentation);
                    }
                }
            }
        } catch (Exception exception){
            logger.error("Exception while processing data from DB.",exception);
        } finally {
            if(resultSet != null){
                resultSet.close();
            }
            if(preparedStatement != null){
                preparedStatement.close();
            }
            if(connection != null){
                connection.close();
            }
        }
        logger.info("userRepresentationMap {}",userRepresentationMap);
        return userRepresentationMap;
    }

    private String getFormattedStringFromCollection(List<String> collect, int offset, int limit) {
        StringBuffer sbf = new StringBuffer();
        sbf.append("select ue.*,ua.name,ua.value  from user_entity ue join user_attribute ua on ua.user_id = ue.id WHERE ue.id IN (");
        collect.stream().forEach(item -> {
            sbf.append("'" + item + "'");
            sbf.append(",");
        });
        String substring = sbf.substring(0, sbf.lastIndexOf(","));
        substring = substring + (") OFFSET " + offset + " LIMIT " + limit);
        logger.info("Query to be Executed {}",substring);
        return substring;
    }

}