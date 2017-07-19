package com.blito.rest.controllers;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.blito.enums.PaymentStatus;
import com.blito.repositories.BlitRepository;
import com.blito.services.PaymentService;

@RestController
public class ZarinpalRestController {
	
	@Value("serverAddress")
	private String serverAddress;
	@Autowired
	private BlitRepository blitRepository;

	@Autowired
	private PaymentService paymentService;
	@GetMapping("${api.base.url}" + "zarinpal")
	public CompletableFuture<?> zarinpalCallback(@RequestParam String Authority,@RequestParam String Status)
	{
		return paymentService.zarinpalPaymentFlow(Authority, Status)
				.handle((blit,throwable) -> {
					if(throwable != null)
					{
						return new RedirectView(String.valueOf(new StringBuilder(serverAddress).append("/payment/error/").append(blit.getTrackCode())));
					}
					return new RedirectView(String.valueOf(new StringBuilder(serverAddress).append("/payment/").append(blit.getTrackCode())));
				});
	}
	
	@GetMapping("/payment/{trackCode}")
	public ModelAndView paymentResult (@PathVariable String trackCode)
	{
		return blitRepository.findByTrackCode(trackCode)
				.map(blit -> {
					if(blit.getPaymentStatus().equals(PaymentStatus.PAID.name()))
						return new ModelAndView("ticket").addObject("blit", blit);
					else
						return new ModelAndView("paymentError").addObject("message","message");
				}).orElseGet(() -> {
					return new ModelAndView("paymentError").addObject("message", "message");
				});
	}
	
	@GetMapping("/payment/error/{trackCode}")
	public ModelAndView ModelAndView(@PathVariable String trackCode)
	{
		return null;
	}
}
