package com.mycompany.payments.controller;

import org.springframework.web.bind.annotation.RestController;

import com.mycompany.payments.pojo.PaymentRequest;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@RestController
@RequestMapping("/v1/payments")
public class PaymentController {
	
    @PostMapping
    public String createPayment(@Valid @RequestBody PaymentRequest paymentReq) {
        
        return "Successful";
    }
}
