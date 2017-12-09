package com.blito.services;

import com.blito.enums.*;
import com.blito.exceptions.BlitNotAvailableException;
import com.blito.exceptions.InconsistentDataException;
import com.blito.exceptions.NotFoundException;
import com.blito.exceptions.PaymentException;
import com.blito.mappers.CommonBlitMapper;
import com.blito.mappers.SeatBlitMapper;
import com.blito.models.Blit;
import com.blito.models.CommonBlit;
import com.blito.models.SeatBlit;
import com.blito.payments.payir.viewmodel.PayDotIrClient;
import com.blito.payments.zarinpal.client.ZarinpalClient;
import com.blito.repositories.BlitRepository;
import com.blito.repositories.CommonBlitRepository;
import com.blito.repositories.DiscountRepository;
import com.blito.repositories.SeatBlitRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.payments.BlitoPaymentResult;
import com.blito.rest.viewmodels.payments.BlitoPaymentVerificationResult;
import com.blito.services.blit.CommonBlitService;
import com.blito.services.blit.SeatBlitService;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.function.BiFunction;

@Service
public class PaymentService {
	private static final Object commonBlitPaymentCompletionLock = new Object();
	private static final Object seatBlitPaymentCompletionLock = new Object();
	private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
	@Autowired
	private BlitRepository blitRepository;
	@Autowired
	private ZarinpalClient zarinpalClient;
	@Autowired
	private CommonBlitRepository commonBlitRepository;
	@Autowired
	private CommonBlitService commonBlitService;
	@Autowired
	private SeatBlitService seatBlitService;
	@Autowired
	private CommonBlitMapper commonBlitMapper;
	@Autowired
	private SeatBlitRepository seatBlitRepository;
	@Autowired
	private SeatBlitMapper seatBlitMapper;
	@Autowired
	private DiscountRepository discountRepository;
	@Autowired
	private PayDotIrClient payDotIrClient;

	@Transactional
	public Blit finalizingPayment(BlitoPaymentResult paymentResult) {
		Blit blit = blitRepository.findByToken(paymentResult.getToken()).orElseThrow(() ->
				new NotFoundException(ResourceUtil.getMessage(Response.BLIT_NOT_FOUND)));
		if(paymentResult.getResult().equals(PayResult.SUCCESS)) {

			log.info("success in '{}' payment callback blit trackCode '{}' user email '{}'",
					blit.getBankGateway(),
					blit.getTrackCode(),
					blit.getCustomerEmail());

			if(blit.getSeatType().equals(SeatType.COMMON.name())) {
				CommonBlit commonBlit = commonBlitRepository.findOne(blit.getBlitId());
				if(commonBlit.getBlitType().getBlitTypeState().equals(State.SOLD.name()))
					throw new BlitNotAvailableException(ResourceUtil.getMessage(Response.BLIT_NOT_AVAILABLE));
				checkBlitCapacity(commonBlit);

				CommonBlit persisted = verifyAndFinalizePayment(commonBlit,(b, verification) -> {
					synchronized (commonBlitPaymentCompletionLock) {
						log.info("User with email '{}' holding the lock after payment",b.getCustomerEmail());
						log.info("User with email '{}' released the lock after payment",b.getCustomerEmail());
						return commonBlitService.finalizeCommonBlitPayment(b, Optional.ofNullable(verification.getRefNum()));
					}
				});
				commonBlitService.sendEmailAndSmsForPurchasedBlit(commonBlitMapper.createFromEntity(persisted));
				return commonBlit;
			} else {
				SeatBlit seatBlit = seatBlitRepository.findOne(blit.getBlitId());
				if(seatBlit.getBlitTypeSeats().stream().anyMatch(blitTypeSeat -> Optional.ofNullable(blitTypeSeat.getReserveDate()).isPresent() && blitTypeSeat.getReserveDate().before(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusMinutes(10L).toInstant())))) {
					throw new BlitNotAvailableException(ResourceUtil.getMessage(Response.SEAT_BLIT_RESERVED_TIME_OUT_OF_DATE));
				}

				SeatBlit persisted = verifyAndFinalizePayment(seatBlit,(s, verification) -> {
					synchronized (seatBlitPaymentCompletionLock) {
						log.info("User with email '{}' holding the lock after payment",seatBlit.getCustomerEmail());
						log.info("User with email '{}' released the lock after payment",seatBlit.getCustomerEmail());
						return seatBlitService.finalizeSeatBlitPayment(seatBlit, Optional.ofNullable(verification.getRefNum()));
					}
				});
				seatBlitService.sendEmailAndSmsForPurchasedBlit(seatBlitMapper.createFromEntity(persisted));
				return seatBlit;
			}
		} else {
			return setError(blit);
		}
	}

	public <B extends Blit> B verifyAndFinalizePayment(B blit,
														BiFunction<B,BlitoPaymentVerificationResult,B> biFunction
													  ) {
		return Try.of(() -> verifyPayment(blit))
				.map(verification -> {
					log.info("success in verifyAndFinalizePayment '{}' verification response trackCode '{}' user email '{}' ref number '{}'",
							blit.getBankGateway(),
							blit.getTrackCode(),
							blit.getCustomerEmail(), verification.getRefNum());
					if(verification.getResult().equals(PayResult.SUCCESS)) {
						return biFunction.apply(blit,verification);
					} else {
						throw new PaymentException(ResourceUtil.getMessage(Response.PAYMENT_ERROR));
					}
				})
				.onFailure(throwable -> {
					log.error("Exception in verifyAndFinalizePayment '{}' payment verification '{}'",blit.getBankGateway(), throwable);
					setError(blit);
				}).getOrElseThrow(() -> new PaymentException(ResourceUtil.getMessage(Response.PAYMENT_ERROR)));
	}

	private Blit setError(Blit blit) {
		blit.setPaymentError(ResourceUtil.getMessage(Response.PAYMENT_ERROR));
		blit.setPaymentStatus(PaymentStatus.ERROR.name());
		return blitRepository.save(blit);
	}

	public <B extends Blit> BlitoPaymentVerificationResult verifyPayment(B blit) {
		switch (Enum.valueOf(BankGateway.class,blit.getBankGateway())) {
			case ZARINPAL:
				return zarinpalClient.getPaymentVerificationResponse(
						blit.getTotalAmount().intValue(),
						blit.getToken())
						.map(BlitoPaymentVerificationResult::transformZarinpalVerificationResponse)
						.recover(throwable -> {
						    log.error("Exception in verifyPayment Zarinpal verification response trackCode '{}', '{}'",blit.getTrackCode(),throwable);
                            return new BlitoPaymentVerificationResult(PayResult.FAILURE);
                        })
						.getOrElse(new BlitoPaymentVerificationResult(PayResult.FAILURE));
			case PAYDOTIR:
				return payDotIrClient.verifyPaymentRequest(Integer.parseInt(blit.getToken()))
						.map(BlitoPaymentVerificationResult::transformPayDotIrVerificationResponse)
						.recover(throwable -> {
                            log.error("Exception in verifyPayment pay.ir verification response trackCode '{}', '{}'",blit.getTrackCode(),throwable);
                            return new BlitoPaymentVerificationResult(PayResult.FAILURE);
                        })
						.getOrElse(new BlitoPaymentVerificationResult(PayResult.FAILURE));
			default:
				throw new NotFoundException(ResourceUtil.getMessage(Response.BANK_GATEWAY_NOT_FOUND));
		}
	}

//	@Transactional
//	public Blit zarinpalPaymentFlow(String authority,String status)
//	{
//		Blit blit = blitRepository.findByToken(authority).orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_NOT_FOUND)));
//		// ********************************************************************************* //
//		if(status.equals("OK"))
//		{
//		// ********************************************************************************* //
//			if(blit.getSeatType().equals(SeatType.COMMON.name()))
//			{
//				log.info("success in zarinpal payment callback blit trackCode '{}' user email '{}'",blit.getTrackCode(),blit.getCustomerEmail());
//				CommonBlit commonBlit = commonBlitRepository.findOne(blit.getBlitId());
//				if(commonBlit.getBlitType().getBlitTypeState().equals(State.SOLD.name()))
//					throw new BlitNotAvailableException(ResourceUtil.getMessage(Response.BLIT_NOT_AVAILABLE));
//				checkBlitCapacity(commonBlit);
//				// ********************************************************************************* //
//				PaymentVerificationResponse verificationResponse = zarinpalClient.getPaymentVerificationResponse(commonBlit.getTotalAmount().intValue(), authority);
//				log.info("success in zarinpal verification response trackCode '{}' user email '{}' ref number '{}'",blit.getTrackCode(),blit.getCustomerEmail(), verificationResponse.getRefID());
//				// ********************************************************************************* //
//				CommonBlit persistedBlit;
//				synchronized (commonBlitPaymentCompletionLock) {
//					log.info("User with email '{}' holding the lock after payment",commonBlit.getCustomerEmail());
//					log.info("Zarinpal message '{}'", ZarinpalException.generateMessage(verificationResponse.getStatus()));
//					persistedBlit = commonBlitService.finalizeCommonBlitPayment(commonBlit, Optional.ofNullable(String.valueOf(verificationResponse.getRefID())));
//					log.info("User with email '{}' released the lock after payment",commonBlit.getCustomerEmail());
//				}
//				commonBlitService.sendEmailAndSmsForPurchasedBlit(commonBlitMapper.createFromEntity(persistedBlit));
//				return persistedBlit;
//			}
//			else {
//				log.info("success in zarinpal payment callback blit trackCode '{}' user email '{}'",blit.getTrackCode(),blit.getCustomerEmail());
//				SeatBlit seatBlit = seatBlitRepository.findOne(blit.getBlitId());
//				if(seatBlit.getBlitTypeSeats().stream().anyMatch(blitTypeSeat -> Optional.ofNullable(blitTypeSeat.getReserveDate()).isPresent() && blitTypeSeat.getReserveDate().before(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusMinutes(10L).toInstant())))) {
//					throw new BlitNotAvailableException(ResourceUtil.getMessage(Response.SEAT_BLIT_RESERVED_TIME_OUT_OF_DATE));
//				}
//				// ********************************************************************************* //
//				PaymentVerificationResponse verificationResponse = zarinpalClient.getPaymentVerificationResponse(seatBlit.getTotalAmount().intValue(),authority);
//				log.info("success in zarinpal verification response trackCode '{}' user email '{}' ref number '{}'",blit.getTrackCode(),blit.getCustomerEmail(), verificationResponse.getRefID());
//				// ********************************************************************************* //
//				SeatBlit persistedSeatBlit;
//				synchronized (seatBlitPaymentCompletionLock) {
//					log.info("User with email '{}' holding the lock after payment",seatBlit.getCustomerEmail());
//					log.info("Zarinpal message '{}'", ZarinpalException.generateMessage(verificationResponse.getStatus()));
//					persistedSeatBlit = seatBlitService.finalizeSeatBlitPayment(seatBlit,Optional.ofNullable(String.valueOf(verificationResponse.getRefID())));
//					log.info("User with email '{}' released the lock after payment",seatBlit.getCustomerEmail());
//				}
//				this.seatBlitService.sendEmailAndSmsForPurchasedBlit(seatBlitMapper.createFromEntity(persistedSeatBlit));
//				return persistedSeatBlit;
//			}
//		}
//		else {
//			log.error("Error in zarinpal callback, blit id '{}' , user email '{}'",blit.getBlitId(),blit.getCustomerEmail());
//			blit.setPaymentError(ResourceUtil.getMessage(Response.PAYMENT_ERROR));
//			blit.setPaymentStatus(PaymentStatus.ERROR.name());
//			return blitRepository.save(blit);
//		}
//	}
	
	private void checkBlitCapacity(CommonBlit blit)
	{
		// TODO: 10/15/17 test 
//		BlitType fethedBlitType = blitTypeRepository.findOne(blit.getBlitType().getBlitTypeId());
		if(blit.getCount() + blit.getBlitType().getSoldCount() > blit.getBlitType().getCapacity())
			throw new InconsistentDataException(
					ResourceUtil.getMessage(Response.BLIT_NOT_AVAILABLE));
	}
}
