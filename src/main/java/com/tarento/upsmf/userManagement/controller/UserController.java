package com.tarento.upsmf.userManagement.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.tarento.upsmf.userManagement.handler.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController(value = "/api/v1/user)")
public class UserController {

   /* @Autowired
    private UserHandler userHandler;*/

    @PostMapping("/create")
    public /*ResponseEntity<JsonNode>*/ void createUser(@RequestBody final JsonNode body) throws URISyntaxException {
        UserHandler userHandler = new UserHandler();
        /*return*/ userHandler.createUser(body);
    }

    @PutMapping("/update/{userId}")
    public /*ResponseEntity<JsonNode>*/ void updateUser(@RequestBody final JsonNode body) throws URISyntaxException {
        UserHandler userHandler = new UserHandler();
        /*return*/ userHandler.updateUser(body);
    }

    @GetMapping("/list")
    public /*ResponseEntity<JsonNode>*/ void listUser(@PathVariable final String userId) throws URISyntaxException {
        UserHandler userHandler = new UserHandler();
        /*return*/ userHandler.listUser(userId);
    }

    @PostMapping("/activate")
    public /*ResponseEntity<JsonNode>*/ void activateUser(@RequestBody final JsonNode body) throws URISyntaxException {
        UserHandler userHandler = new UserHandler();
        /*return*/ userHandler.activateUser(body);
    }

    @PostMapping("/deactivate")
    public /*ResponseEntity<JsonNode>*/ void deactivateUser(@RequestBody final JsonNode body) throws URISyntaxException {
        UserHandler userHandler = new UserHandler();
        /*return*/ userHandler.deactivateUser(body);
    }
}
