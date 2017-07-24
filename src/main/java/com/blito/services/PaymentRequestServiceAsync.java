package com.blito.services;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blito.payments.saman.SamanBankService;
import com.blito.payments.zarinpal.client.ZarinpalClient;

@Service
public class PaymentRequestServiceAsync {
	@Autowired
	private SamanBankService samanBankService;
	@Autowired
	private ZarinpalClient zarinpalClient;

	public CompletableFuture<String> samanBankRequestToken(String reservationNumber, long totalAmount) {
		return samanBankService.requestToken(reservationNumber, totalAmount);
	}

	public CompletableFuture<String> zarinpalRequestToken(int amount, String email, String mobile, String description) {
		return CompletableFuture.supplyAsync(() -> {
			return zarinpalClient.getPaymentRequest(amount, email, mobile, description);
		});
	}
}
