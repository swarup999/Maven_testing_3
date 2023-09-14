package com.tarento.upsmf.userManagement.model;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "user_attribute")
public class UserAttributeModel {

    @Column(name="name")
    private String name;
    @Column(name="value")
    private String value;
    @Column(name="user_id")
    private String userId;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
}
