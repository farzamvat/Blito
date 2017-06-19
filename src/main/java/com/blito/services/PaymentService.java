package com.blito.services;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blito.payments.saman.SamanBankService;
import com.blito.repositories.BlitRepository;

@Service
public class PaymentService {
	@Autowired
	SamanBankService samanBankService;
	@Autowired
	BlitRepository blitRepository;
	
	public CompletableFuture<String> samanBankRequestToken(String reservationNumber,long totalAmount) {
		return samanBankService.requestToken(reservationNumber, totalAmount);
	}
	
	
	
}
