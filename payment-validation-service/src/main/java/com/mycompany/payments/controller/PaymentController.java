package com.mycompany.payments.controller;

import org.springframework.web.bind.annotation.RestController;

import com.mycompany.payments.pojo.PaymentRequest;
import com.mycompany.payments.service.interfaces.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;
	
    @PostMapping
    public String createPayment(@Valid @RequestBody PaymentRequest paymentReq) {

        String serviceResponse = paymentService.validateAndCreatePayment(paymentReq);
        log.info(serviceResponse);
        
        return serviceResponse;
    }
}
