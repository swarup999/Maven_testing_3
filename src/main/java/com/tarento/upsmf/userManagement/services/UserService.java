package com.tarento.upsmf.userManagement.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class UserService {

    public /*ResponseEntity<JsonNode>*/ void createUser(final JsonNode body) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI("https://uphrh.in/api/user/v1/sso/create");
        MultiValueMap headers = new HttpHeaders();
        headers.add("Content-Type","application/json");
        headers.add("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJSR3RkMkZzeG1EMnJER3I4dkJHZ0N6MVhyalhZUzBSSyJ9.kMLn6177rvY53i0RAN3SPD5m3ctwaLb32pMYQ65nBdA");
        HttpEntity<JsonNode> httpEntity = new HttpEntity(body, headers);
        ResponseEntity<String> result = null;
        result = restTemplate.postForEntity(uri,httpEntity,String.class);
        //return result;
    }

    public /*ResponseEntity<JsonNode>*/ void updateUser(final JsonNode body) throws URISyntaxException{
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI("https://uphrh.in/api/user/v1/update");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJSR3RkMkZzeG1EMnJER3I4dkJHZ0N6MVhyalhZUzBSSyJ9.kMLn6177rvY53i0RAN3SPD5m3ctwaLb32pMYQ65nBdA");
        HttpEntity httpEntity = new HttpEntity(body, headers);
        ResponseEntity<String> result = null;
        result = restTemplate.postForEntity(uri,httpEntity,String.class);
        //return result;
    }

    public /*ResponseEntity<JsonNode>*/ void listUser(final String userId) throws URISyntaxException{
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI("https://uphrh.in/api/user/v1/search");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJSR3RkMkZzeG1EMnJER3I4dkJHZ0N6MVhyalhZUzBSSyJ9.kMLn6177rvY53i0RAN3SPD5m3ctwaLb32pMYQ65nBdA");
        HttpEntity httpEntity = new HttpEntity(jsonNode, headers);
        ResponseEntity<String> result = null;
        result = restTemplate.postForEntity(uri,httpEntity,String.class);
        //return result;

    }

    public /*ResponseEntity<JsonNode>*/ void activateUser(final JsonNode body) throws URISyntaxException{
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI("https://uphrh.in/api/user/v1/unblock");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJSR3RkMkZzeG1EMnJER3I4dkJHZ0N6MVhyalhZUzBSSyJ9.kMLn6177rvY53i0RAN3SPD5m3ctwaLb32pMYQ65nBdA");
        HttpEntity httpEntity = new HttpEntity(body, headers);
        ResponseEntity<String> result = null;
        result = restTemplate.postForEntity(uri,httpEntity,String.class);
        //return result;
    }

    public /*ResponseEntity<JsonNode>*/ void deactivateUser(final JsonNode body) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI("https://uphrh.in/api/user/v1/block");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJSR3RkMkZzeG1EMnJER3I4dkJHZ0N6MVhyalhZUzBSSyJ9.kMLn6177rvY53i0RAN3SPD5m3ctwaLb32pMYQ65nBdA");
        HttpEntity httpEntity = new HttpEntity(body, headers);
        ResponseEntity<String> result = null;
        result = restTemplate.postForEntity(uri,httpEntity,String.class);
        //return result;
    }

}
