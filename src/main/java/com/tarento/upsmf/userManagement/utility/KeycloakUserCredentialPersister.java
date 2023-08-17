package com.tarento.upsmf.userManagement.utility;

import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.UUID;

@Component
@PropertySource({ "classpath:application.properties" })
public class KeycloakUserCredentialPersister {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakUserCredentialPersister.class);

    @Autowired
    private Environment env;

    @Autowired
    private KeycloakUserActivateDeActivate keycloakUserActivateDeActivate;

    private static Environment environment;

    private String REGISTRYENDPOINTSAVEUSERINFO;

    @PostConstruct
    public void init(){
        environment = env;
        REGISTRYENDPOINTSAVEUSERINFO = getPropertyValue("registry.endpoint.save.userinfo");
    }

    public static String getPropertyValue(String property){
        return environment.getProperty(property);
    }
    public String persistUserInfo(final String userName, final String password) throws IOException {

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(REGISTRYENDPOINTSAVEUSERINFO);
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        String requestBody = "{" +
            "\"username\": " + "\"" + userName + "\"" + "," +
            "\"password\": " + "\"" + password + "\"" + "," +
        "}";
        StringEntity entity = new StringEntity(requestBody);
        httpPost.setEntity(entity);
        org.apache.http.HttpResponse response = httpClient.execute(httpPost);
        String responseBody = EntityUtils.toString(response.getEntity());
        return responseBody;
    }
}
