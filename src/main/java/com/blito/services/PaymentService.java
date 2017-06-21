package com.blito.services;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.blito.enums.BlitTypeEnum;
import com.blito.enums.PaymentStatus;
import com.blito.enums.State;
import com.blito.models.Blit;
import com.blito.models.BlitType;
import com.blito.payments.localhost._8085.services.FinalizePaymentRequest;
import com.blito.payments.saman.SamanBankService;
import com.blito.repositories.BlitRepository;
import com.blito.repositories.BlitTypeRepository;

@Service
public class PaymentService {
	@Autowired
	SamanBankService samanBankService;
	@Autowired
	BlitRepository blitRepository;
	@Autowired
	BlitTypeRepository blitTypeRepository;

	public CompletableFuture<String> samanBankRequestToken(String reservationNumber, long totalAmount) {
		return samanBankService.requestToken(reservationNumber, totalAmount);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW,isolation=Isolation.SERIALIZABLE)
	private void completePaymentOperation(FinalizePaymentRequest paymentRequestArgs)
	{
		blitRepository.findByTrackCode(paymentRequestArgs.getRequest().getResNum())
		.ifPresent(blit -> {
			if(blit.getType().equals(BlitTypeEnum.COMMON))
			{
				samanBankService.verifyTransaction(paymentRequestArgs.getRequest().getRefNum())
				.thenAccept(res -> {
					blit.setSamanBankRefNumber(paymentRequestArgs.getRequest().getRefNum());
					blit.setSamanTraceNo(paymentRequestArgs.getRequest().getTraceNo());
					blit.setPaymentStatus(PaymentStatus.PAID);
					BlitType blitType = blitTypeRepository.findByBlitTypeId(blit.getBlitId());
					blitType.setSoldCount(blitType.getSoldCount() + blit.getCount());
					if(blitType.getSoldCount() == blitType.getCapacity())
						blitType.setBlitTypeState(State.SOLD);
					blitTypeRepository.save(blitType);
					blitRepository.save(blit);
					//send email asynchronously
				}).exceptionally(t -> {
					samanBankService.revereseTransaction(paymentRequestArgs.getRequest().getRefNum()).join();
					return null;
				});
			}
		});
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
				if(blitOptional.get().getPaymentStatus().equals(PaymentStatus.ERROR)) {
					completePaymentOperation(paymentRequestCallbackArguments);
				}
			}
			else {
				completePaymentOperation(paymentRequestCallbackArguments);
			}
			
		}
		else if(paymentRequestCallbackArguments.getRequest().getState().replaceAll(" ", "").equals("CanceledByUser"))
		{
			//
			//
			//
			//
		}
		else {
			Optional<Blit> blitOptional =
					blitRepository.findByTrackCode(paymentRequestCallbackArguments.getRequest().getResNum());
			blitOptional.ifPresent(blit -> {
				blit.setPaymentError(paymentRequestCallbackArguments.getRequest().getState());
				blit.setSamanTraceNo(paymentRequestCallbackArguments.getRequest().getTraceNo());
				blit.setSamanBankRefNumber(paymentRequestCallbackArguments.getRequest().getRefNum());
				blit.setPaymentStatus(PaymentStatus.ERROR);
				blitRepository.save(blit);
			});
			
		}
	}

}
