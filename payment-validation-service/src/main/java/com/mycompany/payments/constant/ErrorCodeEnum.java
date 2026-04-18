package com.mycompany.payments.constant;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {

// General Errors
    GENERIC_ERROR("10000", "An unexpected error occurred"),


// Root Object Validation
    USER_REQUIRED("10001", "user object must not be null"),
    PAYMENT_REQUIRED("10002", "payment object must not be null"),

    // -------------------------
    // User Object Validations
    // -------------------------

    END_USER_ID_REQUIRED("10100", "endUserID must not be blank"),
    END_USER_ID_TOO_LONG("10101", "endUserID length must be less than 50"),

    FIRSTNAME_REQUIRED("10102", "firstname must not be blank"),
    FIRSTNAME_TOO_LONG("10103", "firstname length must be less than 50"),

    LASTNAME_REQUIRED("10104", "lastname must not be blank"),
    LASTNAME_TOO_LONG("10105", "lastname length must be less than 50"),

    EMAIL_REQUIRED("10106", "email must not be blank"),
    EMAIL_INVALID("10107", "email must be a valid email address"),
    EMAIL_TOO_LONG("10108", "email length must be less than 100"),

    MOBILE_PHONE_REQUIRED("10109", "mobilePhone must not be blank"),
    MOBILE_PHONE_INVALID("10110", "mobilePhone must be a valid phone number"),

    // -------------------------
    // Payment Object Validations
    // -------------------------

    
    AMOUNT_REQUIRED("10202", "amount is required"),
    AMOUNT_INVALID("10203", "amount must be greater than 0"),
    
    BRAND_NAME_REQUIRED("10204", "brandName must not be blank"),
    BRAND_NAME_TOO_LONG("10205", "brandName length must be less than 100"),
    
    LOCALE_REQUIRED("10206", "locale must not be blank"),
    LOCALE_INVALID("10207", "locale must follow format like en-US"),
    
    COUNTRY_REQUIRED("10208", "country must not be blank"),
    COUNTRY_INVALID("10209", "country must be a valid 2-letter ISO code"),
    
    MERCHANT_TXN_REF_REQUIRED("10210", "merchantTxnRef must not be blank"),
    MERCHANT_TXN_REF_TOO_LONG("10211", "merchantTxnRef length must be less than 100"),
    
    PAYMENT_METHOD_REQUIRED("10212", "paymentMethod must not be blank"),
    
    PROVIDER_REQUIRED("10213", "provider must not be blank"),
    
    PAYMENT_TYPE_REQUIRED("10214", "paymentType must not be blank"),
    
    // -------------------------
    // URL Validations
    // -------------------------
    
    SUCCESS_URL_REQUIRED("10300", "successUrl must not be blank"),
    SUCCESS_URL_TOO_LONG("10301", "successUrl length must be less than 500"),
    SUCCESS_URL_INVALID("10302", "successUrl must start with http or https"),
    
    CANCEL_URL_REQUIRED("10303", "cancelUrl must not be blank"),
    CANCEL_URL_TOO_LONG("10304", "cancelUrl length must be less than 500"),
    CANCEL_URL_INVALID("10305", "cancelUrl must start with http or https"),
    
    // -------------------------
    // Line Items
    // -------------------------
    
    LINE_ITEMS_EMPTY("10400", "lineItems must not be empty"),
    
    CURRENCY_REQUIRED("10200", "currency is required"),
    CURRENCY_INVALID("10201", "currency must be a valid 3-letter code"),
    
    PRODUCT_NAME_REQUIRED("10401", "productName is required"),
    PRODUCT_NAME_TOO_LONG("10402", "productName length must be less than 100 characters"),
    
    UNIT_AMOUNT_REQUIRED("10403", "unitAmount is required"),
    UNIT_AMOUNT_INVALID("10404", "unitAmount must be greater than 0"),
    
    QUANTITY_REQUIRED("10405", "quantity is required"),
    QUANTITY_INVALID("10406", "quantity must be at least 1"), 
    FIRSTNAME_CONTAINS_HELLO("100039", "firstname cannot contain 'hello'"), 
    LASTNAME_CONTAINS_HELLO("100040", "lastname cannot contain 'hello'"), 
    AMOUNT_PASS_MAX_LIMIT("100041", "amount cannot be greater than 500"), 
    DUPLICATE_TRANSACTION("100042", "Duplicate transaction detected for the given endUserID and merchantTxnRef"), 
    FAILED_TO_SAVE_PAYMENT_REQUEST("100043", "Failed to save merchant payment request due to an internal error"), 
    INVALID_HMAC("100044", "Invalid HMAC Signature"), 
    MISSING_HMAC("100045", "HMAC Signature not found"), 
    HMAC_COMPUTATION_ERROR("100046", "Error Compution HMAC Signature"), 
    PAYMENT_ATTEMPT_THRESHOLD_EXCEEDED("100047","Payment attempt threshold exceeded. Please try again later.");

    private final String errorCode;
    private final String errorMessage;

    ErrorCodeEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}