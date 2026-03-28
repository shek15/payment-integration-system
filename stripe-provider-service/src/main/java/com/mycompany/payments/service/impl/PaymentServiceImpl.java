package com.mycompany.payments.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mycompany.payments.http.HttpRequest;
import com.mycompany.payments.http.HttpServiceEngine;
import com.mycompany.payments.pojo.CreatePaymentReq;
import com.mycompany.payments.service.helper.CreatePaymentHelper;
import com.mycompany.payments.service.interfaces.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{
    private final HttpServiceEngine httpServiceEngine;
    private final CreatePaymentHelper createPaymentHelper;

    @Override
    public String createPayment(CreatePaymentReq createPaymentReq) {
        log.info("Payment Creation in PaymentServiceImpl");

        HttpRequest httpRequest = createPaymentHelper.preparedStripeCreateSessionRequest(createPaymentReq);

        ResponseEntity<String> response = httpServiceEngine.makeHttpCall(httpRequest);

        log.info("PaymentService created Payment");

        return "\nPaymentService Done" + response.getBody();
    }

}
