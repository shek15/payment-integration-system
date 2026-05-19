package com.mycompany.payments.service.interfaces;

import com.mycompany.payments.pojo.PaymentRequest;

public interface BusinessValidator {
    public void validate(PaymentRequest paymentRequest);
}
