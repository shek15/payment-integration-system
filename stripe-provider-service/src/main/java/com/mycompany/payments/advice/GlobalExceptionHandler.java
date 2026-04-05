package com.mycompany.payments.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mycompany.payments.constant.ErrorCodeEnum;
import com.mycompany.payments.exception.StripeProviderException;
import com.mycompany.payments.pojo.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StripeProviderException.class)
    public ResponseEntity<ErrorResponse> handleStripeProviderException(StripeProviderException ex) {

        HttpStatus httpStatusCode = ex.getHttpStatusCode() != null 
                                    ? ex.getHttpStatusCode() 
                                    : HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(ex.getErrorCode());
        errorResponse.setErrorMessage(ex.getErrorMessage() != null 
                                    ? ex.getErrorMessage() : 
                                    ex.getMessage());

        log.warn("Returning handled StripeProviderException with status {} and error code {}",
            httpStatusCode.value(), errorResponse.getErrorCode());

        return ResponseEntity.status(httpStatusCode.value()).body(errorResponse);
    }


  @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> genericExceptionHandler(NullPointerException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(ErrorCodeEnum.GENERIC_ERROR.getErrorCode());
        errorResponse.setErrorMessage(ErrorCodeEnum.GENERIC_ERROR.getMessage());

        log.error("Unhandled NullPointerException encountered", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
