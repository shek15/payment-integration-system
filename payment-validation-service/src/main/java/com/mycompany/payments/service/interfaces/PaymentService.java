package com.mycompany.payments.service.interfaces;

import com.mycompany.payments.pojo.PaymentRequest;

public interface PaymentService {
    public String validateAndCreatePayment(PaymentRequest paymentRequest);
}
