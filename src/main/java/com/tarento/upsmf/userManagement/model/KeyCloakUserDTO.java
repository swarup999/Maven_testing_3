package com.tarento.upsmf.userManagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode
public class KeyCloakUserDTO implements Serializable {

    @JsonProperty(value = "name", required = true)
    private  String name;
    @JsonProperty(value = "email", required = true)
    private  String email;

    private  String password;
    @JsonProperty(value = "phoneNumber", required = true)
    private  String phoneNumber;
    private  String instituteID;
    private  String instituteName;
    private  String instituteDistrict;
    private  String aadharNumber;
    private  String registerNumber;
    private  String lastName;
    private String role;
    private String activeStatus;
    private String username = UUID.randomUUID().toString();

}
