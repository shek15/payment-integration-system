package com.mycompany.payments.pojo;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Payment {

    @NotBlank(message = "CURRENCY_REQUIRED")
    @Pattern(regexp = "^[A-Z]{3}$", message = "CURRENCY_INVALID")
    private String currency;

    @NotNull(message = "AMOUNT_REQUIRED")
    @Min(value = 1, message = "AMOUNT_INVALID")
    private Integer amount;

    @NotBlank(message = "BRAND_NAME_REQUIRED")
    @Size(max = 100, message = "BRAND_NAME_TOO_LONG")
    private String brandName;

    @NotBlank(message = "LOCALE_REQUIRED")
    @Pattern(regexp = "^[a-z]{2}-[A-Z]{2}$", message = "LOCALE_INVALID")
    private String locale;

    @NotBlank(message = "COUNTRY_REQUIRED")
    @Pattern(regexp = "^[A-Z]{2}$", message = "COUNTRY_INVALID")
    private String country;

    @NotBlank(message = "MERCHANT_TXN_REF_REQUIRED")
    @Size(max = 100, message = "MERCHANT_TXN_REF_TOO_LONG")
    private String merchantTxnRef;

    @NotBlank(message = "PAYMENT_METHOD_REQUIRED")
    private String paymentMethod;

    @NotBlank(message = "PROVIDER_REQUIRED")
    private String provider;

    @NotBlank(message = "PAYMENT_TYPE_REQUIRED")
    private String paymentType;

    @NotBlank(message = "SUCCESS_URL_REQUIRED")
    @Size(max = 500, message = "SUCCESS_URL_TOO_LONG")
    @Pattern(regexp = "^(http|https)://.*$", message = "SUCCESS_URL_INVALID")
    private String successUrl;

    @NotBlank(message = "CANCEL_URL_REQUIRED")
    @Size(max = 500, message = "CANCEL_URL_TOO_LONG")
    @Pattern(regexp = "^(http|https)://.*$", message = "CANCEL_URL_INVALID")
    private String cancelUrl;

    @NotEmpty(message = "LINE_ITEMS_EMPTY")
    @Valid
    private List<LineItem> lineItems;
}
