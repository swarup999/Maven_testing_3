package com.tarento.upsmf.userManagement.utility;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class KeycloakUserUpdater {
    public static void main(String[] args) throws IOException {
        String keycloakBaseUrl = "http://localhost:8080/auth/admin/realms/example/users";
        String accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJUM3F6M0t6TnNYamx2ZmNVX1FKY1R6eHJJUjgwNGlKWnktaE1uSU96dktrIn0.eyJqdGkiOiIyZmYyNjU3NS0yMTE4LTQzYjctYmYxNi05MzIzOTc5ZjAxOTEiLCJleHAiOjE2OTIxMDU5MDMsIm5iZiI6MCwiaWF0IjoxNjkyMTA1ODQzLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvYXV0aC9yZWFsbXMvbWFzdGVyIiwic3ViIjoiM2UxOGExYjMtYTM5YS00MDhhLWIwYmYtMjJhMWJkOGY4ZDk1IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoic2VjdXJpdHktYWRtaW4tY29uc29sZSIsIm5vbmNlIjoiYzU5YzM4NGYtOTY5My00ODdkLTg0OGQtNmFmYmViMGM0ODM4IiwiYXV0aF90aW1lIjoxNjkyMTA0Njk5LCJzZXNzaW9uX3N0YXRlIjoiNTY5NTlhN2QtNjBjYi00YzI5LTg0ZmQtNGNkYjdhYzA0N2U0IiwiYWNyIjoiMSIsInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJtYWhlc2ggbWFuZXkgciIsInByZWZlcnJlZF91c2VybmFtZSI6ImFkbWluIiwiZ2l2ZW5fbmFtZSI6Im1haGVzaCIsImZhbWlseV9uYW1lIjoibWFuZXkgciIsImVtYWlsIjoibWFoZXNoLm1hbmV5QGdtYWlsLmNvbSJ9.LeSON6AeD__ZRI5D2Dexca51tF4vP2BrP4y1H3y_BdrGKy6Gu_QV5bTtjuqjiY8txM-C9ADd7jSXohtpPWHdcgj_bSAaFwWR3U0NliKBusXgSgQpyyCZ5aCkT7JYICZnG2_nvEC6AN1YCuc_hglwEhksWaNoU0KbnjTxnSDxb2HPhM7mYkkObMLZpqpyavYls5JpSaer-n6zNaPh7snyy-EhyXralYaqq0nXJ5uIsIjXxp9NSBchbi4KD-K0xCwRFAWInNkXJcPVKsWv2_nImuUOX_k56ePVQrRZN2KAb7pcuv2T7F4N3gTItt_UUlPdHXMGN3M10M8Ndun_4L7cVw"; // Replace with your access token

        HttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(keycloakBaseUrl);

        httpPut.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        httpPut.setHeader(HttpHeaders.ACCEPT, "application/json, text/plain, */*");
        httpPut.setHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");

        String requestBody = "{" +
            "\"enabled\": true," +
            "\"attributes\": {}," +
            "\"username\": \"def.ghi@yopmail.com\"," +
            "\"emailVerified\": true" +
        "}";

        StringEntity entity = new StringEntity(requestBody);
        httpPut.setEntity(entity);

        HttpResponse response = httpClient.execute(httpPut);
        String responseBody = EntityUtils.toString(response.getEntity());

        if (response.getStatusLine().getStatusCode() == 204) {
            System.out.println("User updated successfully.");
        } else {
            System.out.println("Failed to update user.");
            System.out.println("Response: " + responseBody);
        }
    }
}
