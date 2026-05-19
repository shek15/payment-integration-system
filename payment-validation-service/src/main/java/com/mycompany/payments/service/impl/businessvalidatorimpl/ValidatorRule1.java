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
public class ValidatorRule1 implements BusinessValidator{

    @Override
    public void validate(PaymentRequest paymentRequest) {
        String firstName = paymentRequest.getUser().getFirstname();
        log.info(firstName);

        if (firstName.contains("Hello")) {
            throw new PaymentValidationException(ErrorCodeEnum.FIRSTNAME_CONTAINS_HELLO.getErrorCode(),
            ErrorCodeEnum.FIRSTNAME_CONTAINS_HELLO.getErrorMessage(),
            HttpStatus.BAD_REQUEST);
        }

        log.info("Validation Rule-1 passed.");
    }

}
