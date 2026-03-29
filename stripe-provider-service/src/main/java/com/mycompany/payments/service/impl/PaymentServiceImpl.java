package com.mycompany.payments.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mycompany.payments.http.HttpRequest;
import com.mycompany.payments.http.HttpServiceEngine;
import com.mycompany.payments.pojo.CreatePaymentReq;
import com.mycompany.payments.pojo.PaymentResponse;
import com.mycompany.payments.service.helper.CreatePaymentHelper;
import com.mycompany.payments.service.interfaces.PaymentService;
import com.mycompany.payments.stripe.CheckoutSessionResponse;
import com.mycompany.payments.util.JsonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Orchestrates checkout session creation by preparing the Stripe request,
 * invoking the HTTP client, and mapping the provider response back to the API response.
 */
public class PaymentServiceImpl implements PaymentService {

    private final HttpServiceEngine httpServiceEngine;
    private final CreatePaymentHelper createPaymentHelper;
    private final JsonUtil jsonUtil;

    @Override
    public PaymentResponse createPayment(CreatePaymentReq createPaymentReq) {
        int lineItemCount = createPaymentReq != null && !CollectionUtils.isEmpty(createPaymentReq.getLineItems())
            ? createPaymentReq.getLineItems().size() : 0;

        log.info("Starting payment creation for {} line items", lineItemCount);

        HttpRequest httpRequest = createPaymentHelper.preparedStripeCreateSessionRequest(createPaymentReq);
        log.debug("Prepared Stripe checkout session request for URL {}", httpRequest.getUrl());

        ResponseEntity<String> response = httpServiceEngine.makeHttpCall(httpRequest);
        log.info("Received response from Stripe with status code {}", response.getStatusCode());

        CheckoutSessionResponse checkoutSessionResponse = jsonUtil.convertJsonToObject(
            response.getBody(), CheckoutSessionResponse.class);

        PaymentResponse paymentResponse = mapCheckoutSessionResponseToPaymentResponse(checkoutSessionResponse);

        log.info("Payment session created successfully with session id {}", paymentResponse.getStripeSessionId());

        return paymentResponse;
    }

    /**
     * Maps the provider-specific checkout session response to the public API response model.
     *
     * @param checkoutSessionResponse Stripe checkout session response
     * @return API response containing only the required fields
     */
    public PaymentResponse mapCheckoutSessionResponseToPaymentResponse(CheckoutSessionResponse checkoutSessionResponse) {
        if (checkoutSessionResponse == null) {
            log.warn("Checkout session response is null, unable to map payment response");
            return null;
        }

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setStripeSessionId(checkoutSessionResponse.getId());
        paymentResponse.setHostedPageUrl(checkoutSessionResponse.getUrl());

        log.debug("Mapped checkout session response to payment response for session id {}",
            checkoutSessionResponse.getId());

        return paymentResponse;
    }
}
