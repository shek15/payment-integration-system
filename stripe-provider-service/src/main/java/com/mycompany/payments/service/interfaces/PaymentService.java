package com.mycompany.payments.service.interfaces;

import com.mycompany.payments.pojo.CreatePaymentReq;
import com.mycompany.payments.pojo.PaymentResponse;

/**
 * Defines payment operations exposed by the service layer.
 */
public interface PaymentService {

    /**
     * Creates a payment session with the configured payment provider.
     *
     * @param createPaymentReq request containing redirect URLs and purchasable line items
     * @return response carrying the provider session details needed by the client
     */
    PaymentResponse createPayment(CreatePaymentReq createPaymentReq);
}
