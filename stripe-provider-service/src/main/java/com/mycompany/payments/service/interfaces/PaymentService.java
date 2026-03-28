package com.mycompany.payments.service.interfaces;

import com.mycompany.payments.pojo.CreatePaymentReq;

public interface PaymentService {
    public String createPayment(CreatePaymentReq createPaymentReq);
}
