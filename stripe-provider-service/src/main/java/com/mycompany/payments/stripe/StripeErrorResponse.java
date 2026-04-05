package com.mycompany.payments.stripe;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class StripeErrorResponse {
    @JsonProperty("error")
    private StripeError stripeError;
}
