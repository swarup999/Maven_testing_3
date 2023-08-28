package com.tarento.upsmf.userManagement.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String responseCode;
    private String uniqueRefNumber;
    private Double serviceTaxAmount;
    private Double processingFeeAmount;
    private Double totalAmount;
    private Double transactionAmount;
    private Date transactionDate;
    private String interchangeValue;
    private String tdr;
    private String paymentMode;
    private Integer subMerchantId;
    private String referenceNo;
    private Long entityId;
    private String rs;
    private String tps;
    private String mandatoryFields;
    private String optionalFields;
    private String rsv;
    private String module;

}
