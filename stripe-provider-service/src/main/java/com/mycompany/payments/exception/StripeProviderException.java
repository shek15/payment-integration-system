package com.mycompany.payments.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class StripeProviderException extends RuntimeException {

    private final HttpStatus httpStatusCode;
    private final String errorCode;
    private final String errorMessage;

    public StripeProviderException(HttpStatus httpStatusCode, String errorCode, String errorMessage) {
        super(errorMessage);
        this.httpStatusCode = httpStatusCode;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
