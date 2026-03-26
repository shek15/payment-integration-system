package com.mycompany.payments.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.payments.service.interfaces.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/payments")
public class PaymentController {
	private final PaymentService paymentService;

	@PostMapping
	public String createPayment() {
		log.info("Creating Payment Request");
		String response = paymentService.createPayment();
		log.info("Payment Created: {}", response);
		return "Payment created Successfully!" + response;
	}
	
}
