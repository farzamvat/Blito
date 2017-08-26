package com.blito.rest.controllers;

import com.blito.enums.Response;
import com.blito.exceptions.ResourceNotFoundException;
import com.blito.repositories.BlitRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.services.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("${api.base.url}")
public class ZarinpalRestController {
	
	@Value("${serverAddress}")
	private String serverAddress;
	@Value("${api.base.url}")
	private String baseUrl;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	BlitRepository blitRepository;

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@GetMapping("/zarinpal")
	public CompletionStage<RedirectView> zarinpalCallback(@RequestParam String Authority, @RequestParam String Status)
	{
		return CompletableFuture.supplyAsync(() -> {
			return paymentService.zarinpalPaymentFlow(Authority, Status);
		}).handle((blit,throwable) -> {
			if(throwable != null)
			{
				log.error("******* ERROR IN ZARINPAL PAYMENT FLOW '{}'",throwable.getCause());
				return blitRepository.findByToken(Authority)
				.map(b -> {
					b.setPaymentError(throwable.getCause().getMessage());
					b = blitRepository.save(b);
					return new RedirectView(String.valueOf(new StringBuilder(serverAddress).append("/payment/").append(b.getTrackCode())));
				}).orElseThrow(() -> new ResourceNotFoundException(ResourceUtil.getMessage(Response.BLIT_NOT_FOUND)));

			}
			//TODO
			return new RedirectView(String.valueOf(new StringBuilder(serverAddress).append("/payment/").append(blit.getTrackCode())));
		});
	}
}
