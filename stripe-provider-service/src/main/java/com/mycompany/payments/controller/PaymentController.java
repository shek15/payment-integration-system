package com.mycompany.payments.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.payments.pojo.CreatePaymentReq;
import com.mycompany.payments.service.interfaces.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public String createPayment(@RequestBody CreatePaymentReq createPaymentReq) {
        log.info("Creating Payment");
        String paymentReq = paymentService.createPayment(createPaymentReq);
        log.info("Payment created Successfully: {}", paymentReq);
        return "Payment created successfully!" + paymentReq;
    }
}
