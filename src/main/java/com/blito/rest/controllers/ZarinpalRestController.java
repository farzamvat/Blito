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

	@Autowired
	private PaymentService paymentService;
	@GetMapping("/zarinpal")
	public CompletableFuture<RedirectView> zarinpalCallback(@RequestParam String Authority,@RequestParam String Status)
	{
		return paymentService.zarinpalPaymentFlow(Authority, Status)
				.handle((blit,throwable) -> {
					if(throwable != null)
					{
						return new RedirectView(String.valueOf(new StringBuilder(serverAddress).append("/payment/error/").append(throwable.getCause().getMessage())));
					}
					return new RedirectView(String.valueOf(new StringBuilder(serverAddress).append("/payment/").append(blit.getTrackCode())));
				});
	}
}
