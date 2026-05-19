package com.mycompany.payments.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mycompany.payments.constant.ErrorCodeEnum;
import com.mycompany.payments.exception.PaymentValidationException;
import com.mycompany.payments.pojo.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgsNotValidException(MethodArgumentNotValidException ex) {

        FieldError error = ex.getBindingResult().getFieldErrors().get(0);

        String errorMessage = error.getDefaultMessage();
        ErrorCodeEnum field = ErrorCodeEnum.valueOf(errorMessage);

        ErrorResponse errorResponse = new ErrorResponse(field.getErrorCode(), field.getErrorMessage());

        return ResponseEntity.badRequest().body(errorResponse);

    }

    @ExceptionHandler(PaymentValidationException.class)
    public ResponseEntity<ErrorResponse> paymentValidationException(
        PaymentValidationException paymentValidationException) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(paymentValidationException.getErrorCode());
            errorResponse.setErrorMessage(paymentValidationException.getErrorMessage());

            return ResponseEntity
                .status(paymentValidationException.getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> genericExceptionHandler(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(ErrorCodeEnum.GENERIC_ERROR.getErrorCode());
        errorResponse.setErrorMessage(ErrorCodeEnum.GENERIC_ERROR.getErrorMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
