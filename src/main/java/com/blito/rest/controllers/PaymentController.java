package com.blito.rest.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class PaymentController {
	
	@GetMapping("/payment/{trackCode}")
	public ModelAndView paymentResult (@PathVariable String trackCode)
	{
		return new ModelAndView("index");
	}
	
	@GetMapping("/payment/error/{message}")
	public ModelAndView paymentResultInCaseOfError(@PathVariable String message)
	{
		return new ModelAndView("index");
	}
}
