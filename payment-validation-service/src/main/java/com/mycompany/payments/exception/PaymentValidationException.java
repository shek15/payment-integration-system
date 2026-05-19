package com.mycompany.payments.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class PaymentValidationException extends RuntimeException{
    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    public PaymentValidationException(String errorCode, String errorMessage, HttpStatus httpStatus) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
}
