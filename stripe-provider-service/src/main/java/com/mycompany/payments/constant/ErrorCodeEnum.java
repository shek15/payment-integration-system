package com.mycompany.payments.constant;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {

    // -------- REQUEST --------
    REQUEST_BODY_NULL("30001", "Request body must not be null"),

    // -------- SUCCESS URL --------
    SUCCESS_URL_MISSING("30002", "successUrl is required"),
    SUCCESS_URL_INVALID("30003", "successUrl must be a valid URL"),
    SUCCESS_URL_PATTERN_MISMATCH("30004", "successUrl must contain a valid domain (e.g. example.com)"),

    // -------- CANCEL URL --------
    CANCEL_URL_MISSING("30005", "cancelUrl is required"),
    CANCEL_URL_INVALID("30006", "cancelUrl must be a valid URL"),
    CANCEL_URL_PATTERN_MISMATCH("30007", "cancelUrl must contain a valid domain (e.g. example.com)"),

    // -------- LINE ITEMS --------
    LINE_ITEMS_MISSING("30008", "lineItems must contain at least one item"),
    LINE_ITEM_NULL("30009", "lineItem at index %d must not be null"),

    // -------- CURRENCY --------
    CURRENCY_MISSING("30010", "currency is required for lineItem at index %d"),
    CURRENCY_INVALID("30011", "currency must be a valid 3-letter ISO code for lineItem at index %d"),

    // -------- PRODUCT --------
    PRODUCT_NAME_MISSING("30012", "productName is required for lineItem at index %d"),

    // -------- AMOUNT --------
    UNIT_AMOUNT_INVALID("30013", "unitAmount must be greater than 0 for lineItem at index %d"),

    // -------- QUANTITY --------
    QUANTITY_INVALID("30014", "quantity must be greater than 0 for lineItem at index %d"),

    // -------- GENERIC --------
    GENERIC_ERROR("30015", "An unexpected error occurred"), 
    ERROR_CONNECTING_TO_EXTERNAL_SERVICE("30016", "Error connecting to external service"), 

    // -------- STRIPE ERROR --------
    STRIPE_API_ERROR("30017", "<Dynamic error message should be included>"),
    INVALID_STRIPE_RESPONSE("30018", "Received invalid response from stripe.");

    private final String errorCode;
    private final String errorMessage;

    ErrorCodeEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    // -------- SUPPORT FOR INDEX-BASED MESSAGE --------
    public String getMessage(Object... args) {
        return String.format(this.errorMessage, args);
    }
}
