package com.tarento.upsmf.userManagement.config;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Data
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
@Slf4j
public class KeycloakConfig {

    @Autowired
    private Environment env;

    private static Environment environment;
    String BASE_URL;
    Keycloak keycloak = null;

    private String serverUrl;

    public String realm;

    private String clientId;

    private String clientSecret;

    private String userName;

    private String password;

    private String grantType;

    @PostConstruct
    public void init(){
        environment = env;
        BASE_URL = getPopertyValue("BaseURL");
        serverUrl = getPopertyValue("keycloak.auth-server-url");
        realm = getPopertyValue("keycloak.realm");
        clientId = getPopertyValue("keycloak.client-id");
        clientSecret = getPopertyValue("keycloak.credentials.secret");
        userName = getPopertyValue("keycloak.admin-username");
        password = getPopertyValue("keycloak.admin-password");
        grantType = getPopertyValue("keycloak.grant-type");
    }

    public static String getPopertyValue(String property){
        return environment.getProperty(property);
    }

    public Keycloak getKeycloakInstance() {
        log.info("keycloak real {} and server url {} .", realm, serverUrl);
        if (null == keycloak) {
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .build();
        }
        return keycloak;
    }
}

