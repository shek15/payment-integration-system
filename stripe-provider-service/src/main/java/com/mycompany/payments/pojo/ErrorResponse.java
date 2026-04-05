package com.mycompany.payments.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Standard error response returned by the service.")
@Data
public class ErrorResponse {
    @Schema(description = "Application-specific error code.", example = "30008")
    private String errorCode;

    @Schema(description = "Human-readable error message.", example = "lineItems must contain at least one item")
    private String errorMessage;
}
