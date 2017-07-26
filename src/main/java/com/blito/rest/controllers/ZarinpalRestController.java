package com.blito.rest.controllers;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.blito.services.PaymentService;

@RestController
@RequestMapping("${api.base.url}")
public class ZarinpalRestController {
	
	@Value("${serverAddress}")
	private String serverAddress;
	@Value("${api.base.url}")
	private String baseUrl;

	@Autowired
	private PaymentService paymentService;
	@GetMapping("/zarinpal")
	public CompletableFuture<RedirectView> zarinpalCallback(@RequestParam String Authority,@RequestParam String Status)
	{
		return CompletableFuture.supplyAsync(() -> {
			return paymentService.zarinpalPaymentFlow(Authority, Status);
		}).handle((blit,throwable) -> {
			return new RedirectView(String.valueOf(new StringBuilder(serverAddress).append("/payment/").append(blit.getTrackCode())));
		});
	}
}
