package com.mycompany.payments.stripe;

import lombok.Data;

@Data
public class StripeError {
    private String type;
    private String message;
    private String param;
    private String code;
}
