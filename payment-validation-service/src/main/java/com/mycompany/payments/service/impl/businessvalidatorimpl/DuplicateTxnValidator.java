package com.mycompany.payments.service.impl.businessvalidatorimpl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mycompany.payments.constant.ErrorCodeEnum;
import com.mycompany.payments.entity.MerchantPaymentRequestEntity;
import com.mycompany.payments.exception.PaymentValidationException;
import com.mycompany.payments.pojo.PaymentRequest;
import com.mycompany.payments.repository.interfaces.MerchantPaymentRequestRepository;
import com.mycompany.payments.service.interfaces.BusinessValidator;
import com.mycompany.payments.util.JsonUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor 
public class DuplicateTxnValidator implements BusinessValidator{

    private final MerchantPaymentRequestRepository merchantPaymentRequestRepository;
    private final JsonUtil jsonUtil;

    @Override
    public void validate(PaymentRequest paymentRequest) {

        MerchantPaymentRequestEntity merchantPaymentRequestEntity =
        new MerchantPaymentRequestEntity();

        merchantPaymentRequestEntity.setEndUserId(paymentRequest.getUser().getEndUserID());
        merchantPaymentRequestEntity.setMerchantTxnReference(paymentRequest.getPayment().getMerchantTxnRef());
        merchantPaymentRequestEntity.setTransactionRequest(jsonUtil.convertObjectToJson(paymentRequest));

        int pkId = merchantPaymentRequestRepository.saveMerchantPaymentRequest(merchantPaymentRequestEntity);

        if (pkId == -1) {

            log.error("Failed to save merchant payment request, possible duplicate transaction.");

            throw new PaymentValidationException(
                ErrorCodeEnum.DUPLICATE_TRANSACTION.getErrorCode(), 
                ErrorCodeEnum.DUPLICATE_TRANSACTION.getErrorMessage(), 
                HttpStatus.BAD_REQUEST
            );

        }

        log.info(
            "Payment request is valid,"
            + "no duplicate transaction is detected, "
            + "payment request: {}", 
            paymentRequest
        );
        
    }

}
