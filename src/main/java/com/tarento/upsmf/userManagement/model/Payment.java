package com.tarento.upsmf.userManagement.model;


import lombok.*;

import javax.persistence.*;
import javax.persistence.OneToMany.*;
import java.util.List;



@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames = true)
@Builder
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    private String feeId;
    private String fullName;
    private String noOfExams;
    private String feeAmount;
}