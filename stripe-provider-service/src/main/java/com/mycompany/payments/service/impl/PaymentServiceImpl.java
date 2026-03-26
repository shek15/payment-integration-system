package com.mycompany.payments.service.impl;

import org.springframework.stereotype.Service;

import com.mycompany.payments.service.interfaces.PaymentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService{

    @Override
    public String createPayment() {
        log.info("Processing payment creation logic....");
        return "Payment created with ID 1234";
    }

}
