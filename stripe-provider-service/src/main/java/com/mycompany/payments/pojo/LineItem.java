package com.mycompany.payments.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "One product entry included in the Stripe Checkout session.")
@Data
public class LineItem {
    @Schema(description = "Display name for the product.", example = "Premium Plan")
    private String productName;

    @Schema(description = "Three-letter ISO currency code.", example = "USD")
    private String currency;

    @Schema(description = "Amount in the smallest currency unit.", example = "1999")
    private int unitAmount;

    @Schema(description = "Quantity of the product.", example = "1")
    private int quantity;
}
