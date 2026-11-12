package com.mycompany.payments.entity;

import java.sql.Timestamp;

import lombok.Data;

@Data 
public class MerchantPaymentRequestEntity {

    private Integer id;

    private String endUserId;

    private String merchantTxnReference;

    private String transactionRequest;
    
    private Timestamp creationDate;

}
