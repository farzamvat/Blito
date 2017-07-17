package com.blito.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blito.services.PaymentService;

@RestController
@RequestMapping("${api.base.url}" + "/zarinpal")
public class ZarinpalRestController {

	@Autowired
	private PaymentService paymentService;
	@GetMapping
	public ResponseEntity<?> zarinpalCallback(@RequestParam String Authority,@RequestParam String Status)
	{
		paymentService.zarinpalPaymentFlow(Authority, Status);
		return ResponseEntity.ok().build();
	}
}
