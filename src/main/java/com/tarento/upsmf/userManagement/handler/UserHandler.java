package com.tarento.upsmf.userManagement.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.tarento.upsmf.userManagement.model.KeyCloakUserDTO;
import com.tarento.upsmf.userManagement.services.KeyCloakUsers;
import com.tarento.upsmf.userManagement.services.UserService;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@Component
public class UserHandler {

    private static final Logger logger = LoggerFactory.getLogger(UserHandler.class);
    @Autowired
    private UserService userService;

    @Autowired
    private KeyCloakUsers keyCloakUsers;

    public ResponseEntity<JsonNode>  createUser(final JsonNode body) throws URISyntaxException {
        logger.info("creating user with payload {} ", body.toPrettyString());
        ResponseEntity<JsonNode> user = userService.createUser(body);

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
        javax.ws.rs.core.Response response = keyCloakUsers.createUser(keyCloakUserDTO);
        return user;
    }

    public ResponseEntity<JsonNode>  updateUser(final JsonNode body) throws URISyntaxException{
        logger.info("updating user with payload {} ", body.toPrettyString());
        ResponseEntity<JsonNode> jsonNodeResponseEntity = userService.updateUser(body);

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
        keyCloakUsers.updateUser(keyCloakUserDTO);
        return jsonNodeResponseEntity;
    }

    public List<UserRepresentation> listUser(final JsonNode body) throws URISyntaxException{
        logger.info("creating user with payload {} ", body.toPrettyString());
        ResponseEntity<JsonNode> jsonNodeResponseEntity = userService.listUser(body);
        List<UserRepresentation> listUsers = keyCloakUsers.listUser();
        return listUsers;
    }

    public ResponseEntity<JsonNode>  activateUser(final JsonNode body) throws URISyntaxException{
        logger.info("creating user with payload {} ", body.toPrettyString());
        ResponseEntity<JsonNode> jsonNodeResponseEntity = userService.activateUser(body);
        keyCloakUsers.activateUser(body.get("").asText());
        return jsonNodeResponseEntity;
    }

    public ResponseEntity<JsonNode>  deactivateUser(final JsonNode body) throws URISyntaxException{
        logger.info("creating user with payload {} ", body.toPrettyString());
        ResponseEntity<JsonNode> jsonNodeResponseEntity = userService.deactivateUser(body);
        keyCloakUsers.deactivateUser(body.get("").asText());
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
