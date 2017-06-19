package com.blito.services;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blito.enums.PaymentStatus;
import com.blito.models.Blit;
import com.blito.payments.localhost._8085.services.FinalizePaymentRequest;
import com.blito.payments.saman.SamanBankService;
import com.blito.repositories.BlitRepository;

@Service
public class PaymentService {
	@Autowired
	SamanBankService samanBankService;
	@Autowired
	BlitRepository blitRepository;

	public CompletableFuture<String> samanBankRequestToken(String reservationNumber, long totalAmount) {
		return samanBankService.requestToken(reservationNumber, totalAmount);
	}

	@Transactional
	public void samanPaymentCallback(FinalizePaymentRequest paymentRequestCallbackArguments)
	{
		if(paymentRequestCallbackArguments.getRequest().getState().equals("OK"))
		{
			Optional<Blit> blitOptional = 
					blitRepository.findBySamanBankRefNumber(paymentRequestCallbackArguments.getRequest().getRefNum());
			if(blitOptional.isPresent())
			{
				
			}
			else {
				blitRepository.findByTrackCode(paymentRequestCallbackArguments.getRequest().getResNum())
				.ifPresent(blit -> {
					blit.setSamanBankRefNumber(paymentRequestCallbackArguments.getRequest().getRefNum());
					blit.setSamanTraceNo(paymentRequestCallbackArguments.getRequest().getTraceNo());
					blit.setPaymentStatus(PaymentStatus.PAID);
					//sending email
				});
			}
			
		}
		else if(paymentRequestCallbackArguments.getRequest().getState().replaceAll(" ", "").equals("CanceledByUser"))
		{
			
		}
		else {
			Optional<Blit> blitOptional =
					blitRepository.findByTrackCode(paymentRequestCallbackArguments.getRequest().getResNum());
			blitOptional.ifPresent(blit -> {
				blit.setPaymentError(paymentRequestCallbackArguments.getRequest().getState());
				blit.setSamanTraceNo(paymentRequestCallbackArguments.getRequest().getTraceNo());
				blit.setSamanBankRefNumber(paymentRequestCallbackArguments.getRequest().getRefNum());
				blit.setPaymentStatus(PaymentStatus.ERROR);
			});
			
		}
	}

}
