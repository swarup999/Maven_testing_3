package com.tarento.upsmf.userManagement.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.tarento.upsmf.userManagement.model.KeyCloakUserDTO;
import com.tarento.upsmf.userManagement.services.UserService;
import com.tarento.upsmf.userManagement.utility.KeycloakUserActivateDeActivate;
import com.tarento.upsmf.userManagement.utility.KeycloakUserCreator;
import com.tarento.upsmf.userManagement.utility.KeycloakUserGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

@Component
public class UserHandler {

    private static final Logger logger = LoggerFactory.getLogger(UserHandler.class);
    @Autowired
    private UserService userService;

    @Autowired
    private KeycloakUserCreator keycloakUserCreator;

    @Autowired
    private KeycloakUserGetter keycloakUserGetter;

    @Autowired
    private KeycloakUserActivateDeActivate keycloakUserActivateDeActivate;

    public ResponseEntity<JsonNode>  createUser(final JsonNode body) throws URISyntaxException, IOException {
        logger.info("creating user with payload {} ", body.toPrettyString());

        JsonNode request = body.get("request");
        KeyCloakUserDTO keyCloakUserDTO = KeyCloakUserDTO.builder()
                .email(getorDefault(request,"email",""))
                .name(getorDefault(request,"name",""))
                .instituteName(getorDefault(request,"instituteName",""))
                .instituteID(getorDefault(request,"instituteID",""))
                .lastName(getorDefault(request,"lastName",""))
                .instituteDistrict(getorDefault(request,"instituteDistrict",""))
                .password(getorDefault(request,"password",""))
                .role(getorDefault(request,"role",""))
                .aadharNumber(getorDefault(request,"aadharNumber",""))
                .phoneNumber(getorDefault(request,"phoneNumber",""))
                .registerNumber(getorDefault(request,"registerNumber",""))
                .activeStatus(getorDefault(request,"activeStatus",""))
                .username(UUID.randomUUID().toString())
                .build();
        logger.info("creating user in keycloak with payload {} ", keyCloakUserDTO);
        String respone = keycloakUserCreator.createUser(body);
        logger.info("user created ? {}", respone);
        ResponseEntity<JsonNode> user = userService.createUser(body);
        return user;
    }

    public ResponseEntity<JsonNode>  updateUser(final JsonNode body) throws URISyntaxException, IOException {

        JsonNode request = body.get("request");
        KeyCloakUserDTO keyCloakUserDTO = KeyCloakUserDTO.builder()
                .email(getorDefault(request,"email",""))
                .name(getorDefault(request,"name",""))
                .instituteName(getorDefault(request,"instituteName",""))
                .instituteID(getorDefault(request,"instituteID",""))
                .lastName(getorDefault(request,"lastName",""))
                .instituteDistrict(getorDefault(request,"instituteDistrict",""))
                .password(getorDefault(request,"password",""))
                .role(getorDefault(request,"role",""))
                .aadharNumber(getorDefault(request,"aadharNumber",""))
                .phoneNumber(getorDefault(request,"phoneNumber",""))
                .username(getorDefault(request,"username",""))
                .registerNumber(getorDefault(request,"registerNumber",""))
                .activeStatus(getorDefault(request,"activeStatus",""))
                .build();
        logger.info("updating user in keycloak with payload {} ", keyCloakUserDTO);
        String userName = keycloakUserGetter.findUser(request.get("userName").asText());

        logger.info("updating user with payload {} ", body.toPrettyString());
        ResponseEntity<JsonNode> jsonNodeResponseEntity = userService.updateUser(body);

        return null;
    }

    public String userDetails(final JsonNode body) throws IOException {
        JsonNode request = body.get("request");
        String userName = keycloakUserGetter.findUser(request.get("userName").asText());
        return userName;
    }


    public String listUser(final JsonNode body) throws URISyntaxException, IOException {
        logger.info("creating user with payload {} ", body.toPrettyString());
        String users = keycloakUserGetter.findUser(null);

        ResponseEntity<JsonNode> jsonNodeResponseEntity = userService.listUser(body);

        return users;
    }

    public ResponseEntity<JsonNode>  activateUser(final JsonNode body) throws URISyntaxException, IOException {
        logger.info("activating user with payload {} ", body.toPrettyString());
        JsonNode request = body.get("request");
        keycloakUserActivateDeActivate.activateDeactivatUser(request.get("userName").asText(), true);

        ResponseEntity<JsonNode> jsonNodeResponseEntity = userService.activateUser(body);
        return jsonNodeResponseEntity;
    }

    public ResponseEntity<JsonNode>  deactivateUser(final JsonNode body) throws URISyntaxException, IOException {
        logger.info("deactivating user with payload {} ", body.toPrettyString());

        JsonNode request = body.get("request");
        keycloakUserActivateDeActivate.activateDeactivatUser(request.get("userName").asText(), false);

        ResponseEntity<JsonNode> jsonNodeResponseEntity = userService.deactivateUser(body);
        return jsonNodeResponseEntity;
    }

    public ResponseEntity<String>  sendOTP(String phoneNumber, String name, String otp) throws URISyntaxException {
        logger.info("sending to name: {} OTP:{} to phone: {} ", name, phoneNumber,otp);
        ResponseEntity<String> stringResponseEntity = userService.sendOTP(phoneNumber, name, otp);
        return stringResponseEntity;
    }

    public ResponseEntity<String> generateOTP(String email) throws URISyntaxException {
        logger.info("generating otp to {} ", email);
        ResponseEntity<String> stringResponseEntity = userService.generateOTP(email);
        return stringResponseEntity;
    }

    public ResponseEntity<String> login(final JsonNode body) throws URISyntaxException {
        logger.info("login called with payload {} ", body.toPrettyString());
        return userService.login(body);
    }

    private String getorDefault(final JsonNode request, final String key, final String defaultValue){
        return request.get(key) != null ? request.get(key).asText() : defaultValue;
    }
}
