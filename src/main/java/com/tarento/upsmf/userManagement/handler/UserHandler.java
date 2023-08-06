package com.tarento.upsmf.userManagement.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.tarento.upsmf.userManagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.net.URISyntaxException;

@Component
public class UserHandler {

    /*@Autowired
    private UserService userService;*/

    public /*ResponseEntity<JsonNode>*/ void createUser(final JsonNode body) throws URISyntaxException {
        UserService userService = new UserService();
        /*return*/ userService.createUser(body);
    }

    public /*ResponseEntity<JsonNode>*/ void updateUser(final JsonNode body) throws URISyntaxException{
        UserService userService = new UserService();
        /*return*/ userService.updateUser(body);
    }

    public /*ResponseEntity<JsonNode>*/ void listUser(final String userId) throws URISyntaxException{
        UserService userService = new UserService();
        /*return*/ userService.listUser(userId);
    }

    public /*ResponseEntity<JsonNode>*/ void activateUser(final JsonNode body) throws URISyntaxException{
        UserService userService = new UserService();
        /*return*/ userService.activateUser(body);
    }

    public /*ResponseEntity<JsonNode>*/ void deactivateUser(final JsonNode body) throws URISyntaxException{
        UserService userService = new UserService();
        /*return*/ userService.deactivateUser(body);
    }

}
