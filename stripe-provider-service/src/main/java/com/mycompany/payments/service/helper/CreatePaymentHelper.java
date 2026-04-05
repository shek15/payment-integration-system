package com.mycompany.payments.service.helper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.mycompany.payments.http.HttpRequest;
import com.mycompany.payments.constant.Constants;
import com.mycompany.payments.constant.ErrorCodeEnum;
import com.mycompany.payments.exception.StripeProviderException;
import com.mycompany.payments.pojo.CreatePaymentReq;
import com.mycompany.payments.pojo.LineItem;
import com.mycompany.payments.stripe.CheckoutSessionResponse;
import com.mycompany.payments.stripe.StripeError;
import com.mycompany.payments.stripe.StripeErrorResponse;
import com.mycompany.payments.util.JsonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Builds Stripe-specific request payloads from the incoming payment request.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CreatePaymentHelper {

    private final JsonUtil jsonUtil;

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

    /**
     * Translates Stripe HTTP responses into either a checkout session response or a service exception.
     *
     * @param response raw response returned from Stripe
     * @return parsed checkout session response
     */
    public CheckoutSessionResponse processStripeResponse(ResponseEntity<String> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            CheckoutSessionResponse checkoutSessionResponse = jsonUtil.convertJsonToObject(
                response.getBody(), CheckoutSessionResponse.class);

            if (checkoutSessionResponse != null &&
                checkoutSessionResponse.getUrl() != null) {
                log.debug("Stripe checkout session parsed successfully with session id {}", checkoutSessionResponse.getId());
                return checkoutSessionResponse;
            }

            log.error("Stripe success response could not be parsed into a valid checkout session");

        }

        if (response.getStatusCode().is4xxClientError() ||
            response.getStatusCode().is5xxServerError()) {
            StripeErrorResponse stripeErrorResponse = jsonUtil.convertJsonToObject(response.getBody(),
                StripeErrorResponse.class);

            if (stripeErrorResponse != null && stripeErrorResponse.getStripeError() != null) {
                String stripeErrorMessage = prepareStripeErrorMessage(stripeErrorResponse);
                log.warn("Stripe returned {} with error: {}", response.getStatusCode().value(), stripeErrorMessage);

                throw new StripeProviderException(HttpStatus.valueOf(response.getStatusCode().value()),
                    ErrorCodeEnum.STRIPE_API_ERROR.getErrorCode(),
                    stripeErrorMessage);
            }

            log.error("Stripe returned {} but the error response could not be parsed", response.getStatusCode().value());
        }

        throw new StripeProviderException(HttpStatus.BAD_GATEWAY, 
            ErrorCodeEnum.INVALID_STRIPE_RESPONSE.getErrorCode(), 
            ErrorCodeEnum.INVALID_STRIPE_RESPONSE.getErrorMessage());
    }

    /**
     * Builds a concise error message from the Stripe error payload for downstream logging and API responses.
     *
     * @param stripeErrorResponse parsed Stripe error response
     * @return pipe-delimited Stripe error details
     */
    public String prepareStripeErrorMessage(StripeErrorResponse stripeErrorResponse) {
        StripeError stripeError = stripeErrorResponse.getStripeError();
        log.debug("Preparing Stripe error message for error type {}", stripeError.getType());
        return Stream.of(stripeError.getType(),
                        stripeError.getMessage(),
                        stripeError.getParam(),
                        stripeError.getCode())
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.joining(" | "));
    }
}
