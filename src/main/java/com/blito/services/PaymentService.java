package com.blito.services;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.blito.enums.PaymentStatus;
import com.blito.enums.Response;
import com.blito.enums.SeatType;
import com.blito.enums.State;
import com.blito.exceptions.BlitNotAvailableException;
import com.blito.exceptions.NotFoundException;
import com.blito.exceptions.ZarinpalException;
import com.blito.models.Blit;
import com.blito.models.BlitType;
import com.blito.models.CommonBlit;
import com.blito.payments._76._143._201._138._80.services.FinalizePaymentRequest;
import com.blito.payments.saman.SamanBankService;
import com.blito.payments.zarinpal.PaymentVerificationResponse;
import com.blito.payments.zarinpal.client.ZarinpalClient;
import com.blito.repositories.BlitRepository;
import com.blito.repositories.BlitTypeRepository;
import com.blito.repositories.CommonBlitRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.services.util.HtmlRenderer;

@Service
public class PaymentService {
	@Autowired
	private SamanBankService samanBankService;
	@Autowired
	private BlitRepository blitRepository;
	@Autowired
	private BlitTypeRepository blitTypeRepository;
	@Autowired
	private ZarinpalClient zarinpalClient;
	@Autowired
	private CommonBlitRepository commonBlitRepository;
	@Autowired
	private HtmlRenderer htmlRenderer;
	@Autowired
	private MailService mailService;
	private final Logger log = LoggerFactory.getLogger(PaymentService.class);

	public CompletableFuture<String> samanBankRequestToken(String reservationNumber, long totalAmount) {
		return samanBankService.requestToken(reservationNumber, totalAmount);
	}

	public CompletableFuture<String> zarinpalRequestToken(int amount, String email, String mobile, String description) {
		return CompletableFuture.supplyAsync(() -> {
			return zarinpalClient.getPaymentRequest(amount, email, mobile, description);
		});
	}
	
	@Transactional
	public CompletableFuture<Blit> zarinpalPaymentFlow(String authority,String status)
	{
		return CompletableFuture.supplyAsync(() -> {
			Blit blit = blitRepository.findByToken(authority).orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_NOT_FOUND)));
			if(status.equals("OK"))
			{
				if(blit.getSeatType().equals(SeatType.COMMON.name()))
				{
					log.info("success in zarinpal payment callback blit trackCode '{}' user email '{}'",blit.getTrackCode(),blit.getCustomerEmail());
					CommonBlit commonBlit = commonBlitRepository.findOne(blit.getBlitId());
					if(!commonBlit.getBlitType().getBlitTypeState().equals(State.OPEN.name()))
						throw new BlitNotAvailableException(ResourceUtil.getMessage(Response.BLIT_NOT_AVAILABLE));
					PaymentVerificationResponse verificationResponse = zarinpalClient.getPaymentVerificationResponse((int)commonBlit.getTotalAmount(), authority);
					log.info("success in zarinpal verification response trackCode '{}' user email '{}' ref number '{}'",blit.getTrackCode(),blit.getCustomerEmail(), verificationResponse.getRefID());
					Blit persistedBlit = persistZarinpalBoughtBlit(commonBlit, authority, String.valueOf(verificationResponse.getRefID()), ZarinpalException.generateMessage(verificationResponse.getStatus()));
					Map<String,Object> map = new HashMap<>();
					map.put("blit", persistedBlit);
					mailService.sendEmail(blit.getCustomerEmail(), htmlRenderer.renderHtml("ticket", map), ResourceUtil.getMessage(Response.BLIT_RECIEPT));
					return blit;
				}
				else {
					// TODO
					return null;
				}
			}
			else {
				log.error("Error in zarinpal callback, blit id '{}' , user email '{}'",blit.getBlitId(),blit.getCustomerEmail());
				blit.setPaymentError(status);
				blit.setPaymentStatus(PaymentStatus.ERROR.name());
				return blitRepository.save(blit);
			}
		});
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	private Blit persistZarinpalBoughtBlit(CommonBlit blit, String authority, String refNum, String paymentMessage) {
		CommonBlit commonBlit = commonBlitRepository.findOne(blit.getBlitId());
		BlitType blitType = blitTypeRepository.findByBlitTypeId(commonBlit.getBlitType().getBlitTypeId());
		commonBlit.setRefNum(refNum);
		commonBlit.setPaymentStatus(PaymentStatus.PAID.name());
		blitType.setSoldCount(blitType.getSoldCount() + commonBlit.getCount());
		if (blitType.getSoldCount() == blitType.getCapacity()) {
			blitType.setBlitTypeState(State.SOLD.name());
			if (blitType.getEventDate().getBlitTypes().stream()
					.allMatch(b -> b.getBlitTypeState().equals(State.SOLD.name()))) {
				blitType.getEventDate().setEventDateState(State.SOLD.name());
			}
			if (blitType.getEventDate().getEvent().getEventDates().stream()
					.allMatch(ed -> ed.getEventDateState().equals(State.SOLD.name()))) {
				blitType.getEventDate().getEvent().setEventState(State.SOLD.name());
				blitType.getEventDate().getEvent().setEventSoldDate(
						Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()));
			}
		}
		return commonBlitRepository.save(commonBlit);
	}

	
	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	private void completePaymentOperation(FinalizePaymentRequest paymentRequestArgs) {
		blitRepository.findByTrackCode(paymentRequestArgs.getRequest().getResNum()).ifPresent(blit -> {
			if (blit.getSeatType().equals(SeatType.COMMON)) {
				samanBankService.verifyTransaction(paymentRequestArgs.getRequest().getRefNum()).thenAccept(res -> {
					blit.setRefNum(paymentRequestArgs.getRequest().getRefNum());
					blit.setSamanTraceNo(paymentRequestArgs.getRequest().getTraceNo());
					blit.setPaymentStatus(PaymentStatus.PAID.name());
					// TODO ERROR
					BlitType blitType = blitTypeRepository.findByBlitTypeId(blit.getBlitId());
					blitType.setSoldCount(blitType.getSoldCount() + blit.getCount());
					if (blitType.getSoldCount() == blitType.getCapacity()) {
						blitType.setBlitTypeState(State.SOLD.name());
						if (blitType.getEventDate().getBlitTypes().stream()
								.allMatch(b -> b.getBlitTypeState().equals(State.SOLD.name()))) {
							blitType.getEventDate().setEventDateState(State.SOLD.name());
						}
						if (blitType.getEventDate().getEvent().getEventDates().stream()
								.allMatch(ed -> ed.getEventDateState().equals(State.SOLD.name()))) {
							blitType.getEventDate().getEvent().setEventState(State.SOLD.name());
							blitType.getEventDate().getEvent().setEventSoldDate(
									Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()));
						}
					}

					blitTypeRepository.save(blitType);
					blitRepository.save(blit);

					// send email asynchronously

				}).exceptionally(t -> {
					samanBankService.revereseTransaction(paymentRequestArgs.getRequest().getRefNum()).join();
					return null;
				});
			}
			// seat blit
		});
	}

	

	@Transactional
	public void samanPaymentCallback(FinalizePaymentRequest paymentRequestCallbackArguments) {
		if (paymentRequestCallbackArguments.getRequest().getState().equals("OK")) {
			Optional<Blit> blitOptional = blitRepository
					.findByRefNum(paymentRequestCallbackArguments.getRequest().getRefNum());
			if (blitOptional.isPresent()) {
				if (blitOptional.get().getPaymentStatus().equals(PaymentStatus.ERROR)) {
					completePaymentOperation(paymentRequestCallbackArguments);
				}
			} else {
				completePaymentOperation(paymentRequestCallbackArguments);
			}

		} else if (paymentRequestCallbackArguments.getRequest().getState().replaceAll(" ", "")
				.equals("CanceledByUser")) {
			//
			//
			//
			//
		} else {
			Optional<Blit> blitOptional = blitRepository
					.findByTrackCode(paymentRequestCallbackArguments.getRequest().getResNum());
			blitOptional.ifPresent(blit -> {
				blit.setPaymentError(paymentRequestCallbackArguments.getRequest().getState());
				blit.setSamanTraceNo(paymentRequestCallbackArguments.getRequest().getTraceNo());
				blit.setRefNum(paymentRequestCallbackArguments.getRequest().getRefNum());
				blit.setPaymentStatus(PaymentStatus.ERROR.name());
				blitRepository.save(blit);
			});

		}
	}

}
