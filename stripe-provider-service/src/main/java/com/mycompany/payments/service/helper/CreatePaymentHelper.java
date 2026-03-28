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

@Service
public class CreatePaymentHelper {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${stripe.checkout.session.url}")
    private String stripeCheckoutSessionUrl;

    @SuppressWarnings("null")
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

        return httpRequest;
    }

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
                body.add(lineItemPrefix + Constants.PRICE_DATA_UNIT_AMOUNT, String.valueOf(lineItem.getUnitAmount()));
                body.add(lineItemPrefix + Constants.QUANTITY, String.valueOf(lineItem.getQuantity()));
            }
        }

        return body;
    }

    @SuppressWarnings("null")
    private void addIfPresent(MultiValueMap<String, String> body, String key, String value) {
        if (StringUtils.hasText(value)) {
            body.add(key, value);
        }
    }
}
