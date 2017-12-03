package com.blito.rest.controllers;

import com.blito.payments.payir.viewmodel.request.PayDotIrCallbackRequest;
import com.blito.rest.viewmodels.payments.BlitoPaymentResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("${api.base.url}")
public class PaymentCallbackController extends AbstractPaymentCallbackController {
	
	@GetMapping("/zarinpal-payment-callback")
	public CompletionStage<RedirectView> zarinpalCallback(@RequestParam String Authority,
														  @RequestParam String Status) {
		return completePayment(Authority,() -> BlitoPaymentResult.transformZarinpal(Authority,Status));
	}

	@PostMapping("/pay-payment-callback")
	public CompletionStage<RedirectView> payDotIrCallback(@ModelAttribute PayDotIrCallbackRequest payload) {
		return completePayment(String.valueOf(payload.getTransId()),() -> BlitoPaymentResult.transformPayDotIr(payload.getStatus(),payload.getTransId(),payload.getMessage()));
	}
}
