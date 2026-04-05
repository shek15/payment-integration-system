package com.mycompany.payments.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Response returned after a Stripe Checkout session is created successfully.")
@Data
public class PaymentResponse {
    @Schema(description = "Stripe Checkout session identifier.", example = "cs_test_a1b2c3d4")
    private String stripeSessionId;

    @Schema(description = "Hosted Stripe Checkout page URL.", example = "https://checkout.stripe.com/c/pay/cs_test_a1b2c3d4")
    private String hostedPageUrl;
}
