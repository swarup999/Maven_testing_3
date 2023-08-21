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
    private Integer feeId;

    private String fullName;

    //@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    //@JoinColumn(name = "fee_id")
    //private List<Exam> exams;

    private Integer noOfExams;
    private Integer feeAmount;
}