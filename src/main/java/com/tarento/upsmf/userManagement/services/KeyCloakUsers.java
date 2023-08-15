package com.tarento.upsmf.userManagement.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.tarento.upsmf.userManagement.config.KeycloakConfig;
import com.tarento.upsmf.userManagement.model.KeyCloakUserDTO;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Component
public class KeyCloakUsers {

    private static final Logger logger = LoggerFactory.getLogger(KeyCloakUsers.class);

    @Autowired
    private KeycloakConfig keycloakConfig;


    public javax.ws.rs.core.Response createUser(final KeyCloakUserDTO keyCloakUserDTO){
        logger.info("creating user {} in keycloak", keyCloakUserDTO);
        Keycloak keycloakInstance = keycloakConfig.getKeycloakInstance();
        UserRepresentation newUser = createUserRepresentation(keyCloakUserDTO);
        UsersResource usersResource = keycloakInstance.realm(keycloakConfig.getRealm()).users();
        return usersResource.create(newUser);
    }

    public void updateUser(final KeyCloakUserDTO keyCloakUserDTO)  {
        logger.info("updating user {} in keycloak", keyCloakUserDTO);
        Keycloak keycloakInstance = keycloakConfig.getKeycloakInstance();
        UserResource userResource = keycloakInstance.realm(keycloakConfig.getRealm()).users().get(keyCloakUserDTO.getUsername());
        if(userResource!=null) {
            UserRepresentation user = userResource.toRepresentation();
            if(keyCloakUserDTO.getName()!=null && !keyCloakUserDTO.getName().isEmpty())
                user.setFirstName(keyCloakUserDTO.getName());

            if(keyCloakUserDTO.getLastName()!=null && !keyCloakUserDTO.getLastName().isEmpty())
                user.setLastName(keyCloakUserDTO.getLastName());

            if(keyCloakUserDTO.getPhoneNumber() != null && !keyCloakUserDTO.getPhoneNumber().isEmpty())
                user.singleAttribute("phoneNumber",keyCloakUserDTO.getPhoneNumber());

            if(keyCloakUserDTO.getEmail() != null && !keyCloakUserDTO.getEmail().isEmpty())
                user.setEmail(keyCloakUserDTO.getEmail());

            userResource.update(user);
        } else {
            logger.info("user {} was not not found. update failed.", keyCloakUserDTO);
        }
    }

    public List<UserRepresentation> listUser() {
        logger.info("listing user in keycloak");
        Keycloak keycloakInstance = keycloakConfig.getKeycloakInstance();
        return keycloakInstance.realm(keycloakConfig.getRealm()).users().list();
    }

    public void activateUser(final String userName){
        logger.info("activating user:{} in keycloak",userName);
        Keycloak keycloakInstance = keycloakConfig.getKeycloakInstance();
        UserResource userResource = keycloakInstance.realm(keycloakConfig.getRealm()).users().get(userName);
        UserRepresentation user = userResource.toRepresentation();
        user.setEnabled(true);
    }

    public void deactivateUser(final String userName) {
        logger.info("deActivating user:{} in keycloak",userName);
        Keycloak keycloakInstance = keycloakConfig.getKeycloakInstance();
        UserResource userResource = keycloakInstance.realm(keycloakConfig.getRealm()).users().get(userName);
        UserRepresentation user = userResource.toRepresentation();
        user.setEnabled(false);
    }

    public UserRepresentation createUserRepresentation(final KeyCloakUserDTO keyCloakUserDTO) {
        UserRepresentation newUser = new UserRepresentation();
        newUser.setEnabled(true);
        newUser.setEmail(keyCloakUserDTO.getEmail());
        newUser.setEmailVerified(true);
        newUser.setUsername(keyCloakUserDTO.getEmail());
        newUser.setFirstName(keyCloakUserDTO.getName());
        newUser.setLastName(keyCloakUserDTO.getLastName());
        newUser.singleAttribute("phoneNumber",keyCloakUserDTO.getPhoneNumber());
        newUser.singleAttribute("aadharNumber",keyCloakUserDTO.getAadharNumber());
        newUser.singleAttribute("activeStatus",keyCloakUserDTO.getActiveStatus());
        newUser.singleAttribute("instituteDistrict",keyCloakUserDTO.getInstituteDistrict());
        newUser.singleAttribute("instituteID",keyCloakUserDTO.getInstituteID());
        newUser.singleAttribute("instituteName",keyCloakUserDTO.getInstituteName());
        newUser.singleAttribute("registerNumber",keyCloakUserDTO.getRegisterNumber());
        List<String> roles = new ArrayList<>();
        roles.add(keyCloakUserDTO.getRole());
        newUser.setRealmRoles(roles);
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(keyCloakUserDTO.getPassword());
        credentialRepresentation.setType("password");
        newUser.setCredentials(Collections.singletonList(credentialRepresentation));
        return newUser;
    }

}
