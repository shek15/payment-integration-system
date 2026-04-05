package com.mycompany.payments.pojo;

import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Request payload used to create a Stripe Checkout session.")
@Data
public class CreatePaymentReq {
    @Schema(description = "Customer redirect URL after successful payment.", example = "https://example.com/payment/success")
    private String successUrl;

    @Schema(description = "Customer redirect URL after the checkout flow is cancelled.", example = "https://example.com/payment/cancel")
    private String cancelUrl;

    @ArraySchema(schema = @Schema(implementation = LineItem.class), arraySchema = @Schema(description = "Line items to include in the checkout session."))
    private List<LineItem> lineItems;
}
