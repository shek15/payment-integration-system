package com.mycompany.payments.pojo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PaymentRequest {

    @NotNull
    @Valid
    User user;

    @NotNull
    @Valid
    Payment payment;
}