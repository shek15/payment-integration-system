package com.mycompany.payments.service.helper;

import java.util.List;

import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.mycompany.payments.http.HttpRequest;
import com.mycompany.payments.constant.Constants;
import com.mycompany.payments.pojo.CreatePaymentReq;
import com.mycompany.payments.pojo.LineItem;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
/**
 * Builds Stripe-specific request payloads from the incoming payment request.
 */
public class CreatePaymentHelper {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${stripe.checkout.session.url}")
    private String stripeCheckoutSessionUrl;

    @SuppressWarnings("null")
    /**
     * Creates the HTTP request object required to call Stripe's checkout session API.
     *
     * @param createPaymentReq incoming payment request
     * @return fully populated HTTP request for the Stripe session endpoint
     */
    public HttpRequest preparedStripeCreateSessionRequest(CreatePaymentReq createPaymentReq) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(stripeSecretKey, "");
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = prepareFormUrlEncoded(createPaymentReq);

        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setHttpMethod(HttpMethod.POST);
        httpRequest.setHttpHeaders(httpHeaders);
        httpRequest.setBody(body);
        httpRequest.setUrl(stripeCheckoutSessionUrl);

        log.debug("Prepared Stripe HTTP request for {} with {} form fields", stripeCheckoutSessionUrl, body.size());

        return httpRequest;
    }

    /**
     * Converts the generic payment request into the form-url-encoded structure expected by Stripe.
     *
     * @param createPaymentReq payment request received by the API
     * @return form-url-encoded key/value pairs for the Stripe API
     */
    public MultiValueMap<String, String> prepareFormUrlEncoded(CreatePaymentReq createPaymentReq) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        addIfPresent(body, Constants.MODE, Constants.PAYMENT);
        addIfPresent(body, Constants.SUCCESS_URL, createPaymentReq.getSuccessUrl());
        addIfPresent(body, Constants.CANCEL_URL, createPaymentReq.getCancelUrl());

        List<LineItem> lineItems = createPaymentReq.getLineItems();
        if (!CollectionUtils.isEmpty(lineItems)) {
            for (int i = 0; i < lineItems.size(); i++) {
                LineItem lineItem = lineItems.get(i);
                String lineItemPrefix = Constants.LINE_ITEMS + "[" + i + "]";

                addIfPresent(body, lineItemPrefix + Constants.PRICE_DATA_CURRENCY, lineItem.getCurrency());
                addIfPresent(body, lineItemPrefix + Constants.PRICE_DATA_PRODUCT_NAME, lineItem.getProductName());
                addIfPresent(body, lineItemPrefix + Constants.PRICE_DATA_UNIT_AMOUNT, String.valueOf(lineItem.getUnitAmount()));
                addIfPresent(body, lineItemPrefix + Constants.QUANTITY, String.valueOf(lineItem.getQuantity()));
            }
        }

        log.debug("Prepared Stripe form body with {} line items", CollectionUtils.isEmpty(lineItems) ? 0 : lineItems.size());

        return body;
    }

    @SuppressWarnings("null")
    private void addIfPresent(MultiValueMap<String, String> body, String key, String value) {
        if (StringUtils.hasText(value)) {
            body.add(key, value);
        } else {
            log.debug("Skipping blank form field {}", key);
        }
    }
}
