package com.tarento.upsmf.userManagement.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.tarento.upsmf.userManagement.model.KeyCloakUserDTO;
import com.tarento.upsmf.userManagement.services.KeyCloakUsers;
import com.tarento.upsmf.userManagement.services.UserService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.net.URISyntaxException;
import java.util.List;

@Component
public class UserHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private KeyCloakUsers keyCloakUsers;

    public ResponseEntity<JsonNode>  createUser(final JsonNode body) throws URISyntaxException {
        ResponseEntity<JsonNode> user = userService.createUser(body);

        KeyCloakUserDTO keyCloakUserDTO = KeyCloakUserDTO.builder()
                .email(body.get("").asText())
                .name(body.get("").asText())
                .instituteName(body.get("").asText())
                .instituteID(body.get("").asText())
                .lastName(body.get("").asText())
                .instituteDistrict(body.get("").asText())
                .password(body.get("").asText())
                .role(body.get("").asText())
                .aadharNumber(body.get("").asText())
                .phoneNumber(body.get("").asText())
                .username(body.get("").asText())
                .registerNumber(body.get("").asText())
                .activeStatus(body.get("").asText())
                .build();

        javax.ws.rs.core.Response response = keyCloakUsers.createUser(keyCloakUserDTO);
        return user;
    }

    public ResponseEntity<JsonNode>  updateUser(final JsonNode body) throws URISyntaxException{
        ResponseEntity<JsonNode> jsonNodeResponseEntity = userService.updateUser(body);
        KeyCloakUserDTO keyCloakUserDTO = KeyCloakUserDTO.builder()
                .email(body.get("").asText())
                .name(body.get("").asText())
                .instituteName(body.get("").asText())
                .instituteID(body.get("").asText())
                .lastName(body.get("").asText())
                .instituteDistrict(body.get("").asText())
                .password(body.get("").asText())
                .role(body.get("").asText())
                .aadharNumber(body.get("").asText())
                .phoneNumber(body.get("").asText())
                .username(body.get("").asText())
                .registerNumber(body.get("").asText())
                .activeStatus(body.get("").asText())
                .build();
        keyCloakUsers.updateUser(keyCloakUserDTO);
        return jsonNodeResponseEntity;
    }

    public List<UserRepresentation> listUser(final JsonNode body) throws URISyntaxException{
        ResponseEntity<JsonNode> jsonNodeResponseEntity = userService.listUser(body);
        List<UserRepresentation> listUsers = keyCloakUsers.listUser();
        return listUsers;
    }

    public ResponseEntity<JsonNode>  activateUser(final JsonNode body) throws URISyntaxException{
        ResponseEntity<JsonNode> jsonNodeResponseEntity = userService.activateUser(body);
        keyCloakUsers.activateUser(body.get("").asText());
        return jsonNodeResponseEntity;
    }

    public ResponseEntity<JsonNode>  deactivateUser(final JsonNode body) throws URISyntaxException{
        ResponseEntity<JsonNode> jsonNodeResponseEntity = userService.deactivateUser(body);
        keyCloakUsers.activateUser(body.get("").asText());
        return jsonNodeResponseEntity;
    }

    public ResponseEntity<String>  sendOTP(String phoneNumber, String name, String otp) throws URISyntaxException {
        ResponseEntity<String> stringResponseEntity = userService.sendOTP(phoneNumber, name, otp);
        return stringResponseEntity;
    }

    public ResponseEntity<String> generateOTP(String email) throws URISyntaxException {
        ResponseEntity<String> stringResponseEntity = userService.generateOTP(email);
        return stringResponseEntity;
    }

    public ResponseEntity<String> login(final JsonNode body) throws URISyntaxException {
        return userService.login(body);
    }
}
