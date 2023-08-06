package com.tarento.upsmf.userManagement.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class UserService {

    @Autowired
    private Environment env;

    private final RestTemplate restTemplate = new RestTemplate();
    final String BASE_URL = "https://uphrh.in/api";

    private HttpHeaders getHeader(){
        System.out.println(env.getProperty("BaseURL"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJSR3RkMkZzeG1EMnJER3I4dkJHZ0N6MVhyalhZUzBSSyJ9.kMLn6177rvY53i0RAN3SPD5m3ctwaLb32pMYQ65nBdA");
        headers.add("x-authenticated-user-token","eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJBbjFzUzNFanlFeUhYRGdlWXUxdGdpTjM2SDdyalFOcld0a3NOQzhnbUNVIn0.eyJqdGkiOiJmODNlZTgzOS05NmM0LTQ3NWMtYTk2Yi1lNDI2MDViYmNjMjMiLCJleHAiOjE2OTA4Mzk2OTgsIm5iZiI6MCwiaWF0IjoxNjkwNzk2NDk4LCJpc3MiOiJodHRwczovL3VwaHJoLmluL2F1dGgvcmVhbG1zL3N1bmJpcmQiLCJhdWQiOlsicmVhbG0tbWFuYWdlbWVudCIsImFjY291bnQiXSwic3ViIjoiYTk3YjQyY2UtNzkwNi00MTRlLWJjMzAtMzFhODk0NjgwZjUzIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoibG1zIiwiYXV0aF90aW1lIjowLCJzZXNzaW9uX3N0YXRlIjoiZjc2NjlhYmItOWE4Ni00MWNjLWI2OWUtM2Q4ZTZjZjE2MjI2IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL3VwaHJoLmluIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsImFkbWluIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJyZWFsbS1tYW5hZ2VtZW50Ijp7InJvbGVzIjpbIm1hbmFnZS11c2VycyIsInZpZXctdXNlcnMiLCJxdWVyeS1ncm91cHMiLCJxdWVyeS11c2VycyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiIiLCJuYW1lIjoiYWRtaW4gYWRtaW4iLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhZG1pbiIsImdpdmVuX25hbWUiOiJhZG1pbiIsImZhbWlseV9uYW1lIjoiYWRtaW4iLCJlbWFpbCI6ImFkbWluQHN1bmJpcmQub3JnIn0.fh9Zj8htV_-ecMWF_5E2zNsZTZCITP509uJk7zSm6o-uqLRLXy6YupbNbyUZmAL_CkkmAawCtQcNA4GTyaacSA4mRBDfWrYeLfrguFbmgTMMuLOLGzfCfnTwDYhAoyZlyO8P8pnw9B8a0nKClqbqt1h2kpGYDmNV7fXltrE4f81IDXGGIbxuWrFhqmqT2xXi4gIf6Y6ANXFoU4jYaqRXw3hoYwCyYDFi57Dljzx_KWfCb4UxKqmVF1vlLokNVoYuhMNZSICEwJCBIS_PrY9sfoYZZK1Q04NWs1F6Hppqy85PhWn_EycaEeZ0OAByIvi2HagdT0Ris2sHVFntphQbkA");
        return headers;
    }

    public ResponseEntity<JsonNode> createUser(final JsonNode body) throws URISyntaxException {
        URI uri = new URI(BASE_URL + "/user/v1/sso/create");
        HttpHeaders headers = getHeader();
        HttpEntity<JsonNode> httpEntity = new HttpEntity(body, headers);
        ResponseEntity<JsonNode> result = restTemplate.postForEntity(uri,httpEntity,JsonNode.class);
        return result;
    }

    public ResponseEntity<JsonNode> updateUser(final JsonNode body) throws URISyntaxException{
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(BASE_URL + "/user/v1/update");
        HttpHeaders headers = getHeader();
        HttpEntity httpEntity = new HttpEntity(body, headers);
        ResponseEntity<JsonNode> result = restTemplate.postForEntity(uri,httpEntity,JsonNode.class);
        return result;
    }

    public ResponseEntity<JsonNode> listUser(final JsonNode body) throws URISyntaxException{
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(BASE_URL + "/user/v1/search");
        HttpHeaders headers = getHeader();
        HttpEntity httpEntity = new HttpEntity(body, headers);
        ResponseEntity<JsonNode> result = restTemplate.postForEntity(uri,httpEntity,JsonNode.class);
        return result;
    }

    public ResponseEntity<JsonNode> activateUser(final JsonNode body) throws URISyntaxException{
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(BASE_URL + "/user/v1/unblock");
        HttpHeaders headers = getHeader();
        HttpEntity httpEntity = new HttpEntity(body, headers);
        ResponseEntity<JsonNode> result = restTemplate.postForEntity(uri,httpEntity,JsonNode.class);
        return result;
    }

    public ResponseEntity<JsonNode> deactivateUser(final JsonNode body) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(BASE_URL + "/user/v1/block");
        HttpHeaders headers = getHeader();
        HttpEntity httpEntity = new HttpEntity(body, headers);
        ResponseEntity<JsonNode> result = restTemplate.postForEntity(uri,httpEntity,JsonNode.class);
        return result;
    }
}
