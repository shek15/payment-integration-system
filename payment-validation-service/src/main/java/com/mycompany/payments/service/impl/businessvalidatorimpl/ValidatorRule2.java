package com.mycompany.payments.service.impl.businessvalidatorimpl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mycompany.payments.constant.ErrorCodeEnum;
import com.mycompany.payments.exception.PaymentValidationException;
import com.mycompany.payments.pojo.PaymentRequest;
import com.mycompany.payments.service.interfaces.BusinessValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ValidatorRule2 implements BusinessValidator{

    @Override
    public void validate(PaymentRequest paymentRequest) {
         String lastName = paymentRequest.getUser().getLastname();
        log.info(lastName);

        if (lastName.contains("Hello")) {
            throw new PaymentValidationException(ErrorCodeEnum.LASTNAME_CONTAINS_HELLO.getErrorCode(),
            ErrorCodeEnum.LASTNAME_CONTAINS_HELLO.getErrorMessage(),
            HttpStatus.BAD_REQUEST);
        }

        log.info("Validation Rule-2 passed.");
    }

}
