package com.mycompany.payments.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.payments.pojo.CreatePaymentReq;
import com.mycompany.payments.pojo.ErrorResponse;
import com.mycompany.payments.pojo.PaymentResponse;
import com.mycompany.payments.service.ValidationService;
import com.mycompany.payments.service.interfaces.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Exposes payment APIs for creating hosted checkout sessions.
 */
@Slf4j
@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final ValidationService validationService;

    /**
     * Accepts a payment request and delegates checkout session creation to the service layer.
     *
     * @param createPaymentReq request containing redirect URLs and line items
     * @return payment response containing the Stripe session id and hosted page URL
     */
    @Operation(
        summary = "Create a Stripe Checkout session",
        description = "Validates the incoming request, creates a Stripe Checkout session, and returns the session id and hosted checkout URL."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Checkout session created successfully",
            content = @Content(schema = @Schema(implementation = PaymentResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request payload",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected internal error",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "502",
            description = "Stripe returned an invalid response",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping
    public PaymentResponse createPayment(@RequestBody CreatePaymentReq createPaymentReq) {
        int lineItemCount = createPaymentReq != null && createPaymentReq.getLineItems() != null
            ? createPaymentReq.getLineItems().size() : 0;

        log.info("Received create payment request with {} line items", lineItemCount);

        validationService.validate(createPaymentReq);
        log.debug("Create payment request validation completed successfully");

        PaymentResponse paymentResponse = paymentService.createPayment(createPaymentReq);

        log.info("Payment created successfully with Stripe session id {}", paymentResponse.getStripeSessionId());

        return paymentResponse;
    }
}
