package com.mycompany.payments.repository.impl;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.mycompany.payments.constant.ErrorCodeEnum;
import com.mycompany.payments.entity.MerchantPaymentRequestEntity;
import com.mycompany.payments.exception.PaymentValidationException;
import com.mycompany.payments.repository.interfaces.MerchantPaymentRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Repository 
@RequiredArgsConstructor 
public class MerchantPaymentRequestRepositoryImp 
implements MerchantPaymentRequestRepository{

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    static final String SQL_INSERT =
    """
        INSERT INTO merchant_payment_request
        (endUserId, merchantTxnReference, transactionRequest)
        VALUES (:endUserId, :merchantTxnReference, :transactionRequest);
    """;
        

    @Override
    public int saveMerchantPaymentRequest(
        MerchantPaymentRequestEntity merchantPaymentRequestEntity) {
        
        MapSqlParameterSource param = new MapSqlParameterSource();

        param.addValue("endUserId", merchantPaymentRequestEntity.getEndUserId());
        param.addValue("merchantTxnReference", merchantPaymentRequestEntity.getMerchantTxnReference());
        param.addValue("transactionRequest", merchantPaymentRequestEntity.getTransactionRequest());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            namedParameterJdbcTemplate.update(
                SQL_INSERT, 
                param,
                keyHolder
            );

            Number generatedId = keyHolder.getKey();

            if (generatedId != null) {

                log.info("The generated key that is unique to the table: {}",generatedId);

                return generatedId.intValue();

            }

            log.info(
                "Failed to retrive generated Id after inserting payment request: {}", 
                merchantPaymentRequestEntity
            );

            throw new PaymentValidationException(
                ErrorCodeEnum.FAILED_TO_SAVE_PAYMENT_REQUEST.getErrorCode(), 
                ErrorCodeEnum.FAILED_TO_SAVE_PAYMENT_REQUEST.getErrorMessage(), 
                HttpStatus.INTERNAL_SERVER_ERROR
            );
            
        } catch (DuplicateKeyException e) {

            log.error("Duplicate merchantTxnReference is detected {}", 
                        merchantPaymentRequestEntity.getMerchantTxnReference(),
                        e
                    );

            return -1;

        }
    }

}
