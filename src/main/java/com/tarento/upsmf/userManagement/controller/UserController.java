package com.tarento.upsmf.userManagement.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.tarento.upsmf.userManagement.handler.UserHandler;
import com.tarento.upsmf.userManagement.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController {

    @Autowired
    PaymentService paymentService;

    @Autowired
    private UserHandler userHandler;

    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
    public String createUser(@RequestBody final JsonNode body) throws URISyntaxException, IOException {
        return userHandler.createUser(body);
    }

    @PutMapping(value = "/update", consumes = "application/json", produces = "application/json")
    public String updateUser(@RequestBody final JsonNode body) throws URISyntaxException, IOException {
        return userHandler.updateUser(body);
    }

    @PostMapping(value = "/details", consumes = "application/json", produces = "application/json")
    public String userDetails(@RequestBody final JsonNode body) throws URISyntaxException, IOException {
        return userHandler.userDetails(body);
    }

    @PostMapping(value = "/list", consumes = "application/json", produces = "application/json")
    public String listUser(@RequestBody final JsonNode body) throws URISyntaxException, IOException {
        return userHandler.listUser(body);
    }

    @PostMapping(value = "/activate", consumes = "application/json", produces = "application/json")
    public String activateUser(@RequestBody final JsonNode body) throws URISyntaxException, IOException {
        return userHandler.activateUser(body);
    }

    @PostMapping(value = "/deactivate", consumes = "application/json", produces = "application/json")
    public String deactivateUser(@RequestBody final JsonNode body) throws URISyntaxException, IOException {
        return userHandler.deactivateUser(body);
    }

    @GetMapping(value = "/sms/otp", produces = "application/json")
    public ResponseEntity<String> sendOTP(@RequestParam String phoneNumber, @RequestParam String name, @RequestParam String otp) throws URISyntaxException {
        return userHandler.sendOTP(phoneNumber, name, otp);
    }

    @PostMapping(value = "/keycloak/otp", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> generateOTP(@RequestBody String email) throws URISyntaxException, IOException {
        return userHandler.generateOTP(email);
    }

    @PostMapping(value = "/keycloak/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> login(@RequestBody JsonNode body ) throws URISyntaxException, IOException {
        return userHandler.login(body);
    }

    @GetMapping(value = "/payment")
    public ResponseEntity<String> paymentRedirect(@RequestParam("feeId") String feeId, @RequestParam("fullName") String fullName, @RequestParam("noOfExams") String noOfExams,
                                                  @RequestParam("feeAmount") String feeAmount) throws URISyntaxException, IOException {
        return userHandler.paymentRedirect(feeId, fullName, noOfExams, feeAmount);
    }
}
