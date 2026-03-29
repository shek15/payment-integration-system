package com.mycompany.payments.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.payments.pojo.CreatePaymentReq;
import com.mycompany.payments.pojo.PaymentResponse;
import com.mycompany.payments.service.interfaces.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
/**
 * Exposes payment APIs for creating hosted checkout sessions.
 */
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Accepts a payment request and delegates checkout session creation to the service layer.
     *
     * @param createPaymentReq request containing redirect URLs and line items
     * @return payment response containing the Stripe session id and hosted page URL
     */
    @PostMapping
    public PaymentResponse createPayment(@RequestBody CreatePaymentReq createPaymentReq) {
        int lineItemCount = createPaymentReq != null && createPaymentReq.getLineItems() != null
            ? createPaymentReq.getLineItems().size() : 0;

        log.info("Received create payment request with {} line items", lineItemCount);

        PaymentResponse paymentResponse = paymentService.createPayment(createPaymentReq);

        log.info("Payment created successfully with stripe session id {}", paymentResponse.getStripeSessionId());

        return paymentResponse;
    }
}
