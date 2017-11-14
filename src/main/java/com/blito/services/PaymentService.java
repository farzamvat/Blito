package com.blito.services;

import com.blito.enums.*;
import com.blito.exceptions.BlitNotAvailableException;
import com.blito.exceptions.InconsistentDataException;
import com.blito.exceptions.NotFoundException;
import com.blito.exceptions.ZarinpalException;
import com.blito.mappers.CommonBlitMapper;
import com.blito.mappers.SeatBlitMapper;
import com.blito.models.*;
import com.blito.payments.saman.SamanBankService;
import com.blito.payments.zarinpal.PaymentVerificationResponse;
import com.blito.payments.zarinpal.client.ZarinpalClient;
import com.blito.repositories.BlitRepository;
import com.blito.repositories.BlitTypeRepository;
import com.blito.repositories.CommonBlitRepository;
import com.blito.repositories.SeatBlitRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.services.blit.CommonBlitService;
import com.blito.services.blit.SeatBlitService;
import com.blito.services.util.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {
	private static final Object commonBlitPaymentCompletionLock = new Object();
	private static final Object seatBlitPaymentCompletionLock = new Object();
	private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
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
	@Autowired
	private SmsService smsService;
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

	@Transactional
	public Blit zarinpalPaymentFlow(String authority,String status)
	{
		Blit blit = blitRepository.findByToken(authority).orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_NOT_FOUND)));
		if(status.equals("OK"))
		{
			if(blit.getSeatType().equals(SeatType.COMMON.name()))
			{
				log.info("success in zarinpal payment callback blit trackCode '{}' user email '{}'",blit.getTrackCode(),blit.getCustomerEmail());
				CommonBlit commonBlit = commonBlitRepository.findOne(blit.getBlitId());
				if(commonBlit.getBlitType().getBlitTypeState().equals(State.SOLD.name()))
					throw new BlitNotAvailableException(ResourceUtil.getMessage(Response.BLIT_NOT_AVAILABLE));
				checkBlitCapacity(commonBlit);
				PaymentVerificationResponse verificationResponse = zarinpalClient.getPaymentVerificationResponse(commonBlit.getTotalAmount().intValue(), authority);
				log.info("success in zarinpal verification response trackCode '{}' user email '{}' ref number '{}'",blit.getTrackCode(),blit.getCustomerEmail(), verificationResponse.getRefID());
				CommonBlit persistedBlit;
				synchronized (commonBlitPaymentCompletionLock) {
					log.info("User with email '{}' holding the lock after payment",commonBlit.getCustomerEmail());
					log.info("Zarinpal message '{}'", ZarinpalException.generateMessage(verificationResponse.getStatus()));
					persistedBlit = finalizeCommonBlitPayment(commonBlit, String.valueOf(verificationResponse.getRefID()));
					log.info("User with email '{}' released the lock after payment",commonBlit.getCustomerEmail());
				}
				this.commonBlitService.sendEmailAndSmsForPurchasedBlit(commonBlitMapper.createFromEntity(persistedBlit));
				return persistedBlit;
			}
			else {
				log.info("success in zarinpal payment callback blit trackCode '{}' user email '{}'",blit.getTrackCode(),blit.getCustomerEmail());
				SeatBlit seatBlit = seatBlitRepository.findOne(blit.getBlitId());
				if(seatBlit.getBlitTypeSeats().stream().anyMatch(blitTypeSeat -> Optional.ofNullable(blitTypeSeat.getReserveDate()).isPresent() && blitTypeSeat.getReserveDate().before(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusMinutes(10L).toInstant())))) {
					throw new BlitNotAvailableException(ResourceUtil.getMessage(Response.SEAT_BLIT_RESERVED_TIME_OUT_OF_DATE));
				}
				PaymentVerificationResponse verificationResponse = zarinpalClient.getPaymentVerificationResponse(seatBlit.getTotalAmount().intValue(),authority);
				log.info("success in zarinpal verification response trackCode '{}' user email '{}' ref number '{}'",blit.getTrackCode(),blit.getCustomerEmail(), verificationResponse.getRefID());
				SeatBlit persistedSeatBlit;
				synchronized (seatBlitPaymentCompletionLock) {
					log.info("User with email '{}' holding the lock after payment",seatBlit.getCustomerEmail());
					log.info("Zarinpal message '{}'", ZarinpalException.generateMessage(verificationResponse.getStatus()));
					persistedSeatBlit = finalizeSeatBlitPayment(seatBlit,String.valueOf(verificationResponse.getRefID()));
					log.info("User with email '{}' released the lock after payment",seatBlit.getCustomerEmail());
				}
				this.seatBlitService.sendEmailAndSmsForPurchasedBlit(seatBlitMapper.createFromEntity(persistedSeatBlit));
				return persistedSeatBlit;
			}
		}
		else {
			log.error("Error in zarinpal callback, blit id '{}' , user email '{}'",blit.getBlitId(),blit.getCustomerEmail());
			blit.setPaymentError(ResourceUtil.getMessage(Response.PAYMENT_ERROR));
			blit.setPaymentStatus(PaymentStatus.ERROR.name());
			return blitRepository.save(blit);
		}
	}
	
	private void checkBlitCapacity(CommonBlit blit)
	{
		// TODO: 10/15/17 test 
//		BlitType fethedBlitType = blitTypeRepository.findOne(blit.getBlitType().getBlitTypeId());
		if(blit.getCount() + blit.getBlitType().getSoldCount() > blit.getBlitType().getCapacity())
			throw new InconsistentDataException(
					ResourceUtil.getMessage(Response.BLIT_NOT_AVAILABLE));
	}
	@Transactional
	public CommonBlit finalizeCommonBlitPayment(CommonBlit commonBlit, String refNum) {
		BlitType blitType = commonBlit.getBlitType();
		commonBlit.setRefNum(refNum);
		commonBlit.setCreatedAt(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()));
		commonBlit.setPaymentStatus(PaymentStatus.PAID.name());
		commonBlit.setPaymentError(ResourceUtil.getMessage(Response.PAYMENT_SUCCESS));
		blitType.setSoldCount(blitType.getSoldCount() + commonBlit.getCount());
		log.info("****** NONE FREE COMMON BLIT SOLD COUNT RESERVED BY USER '{}' SOLD COUNT IS '{}' AND BLIT TYPE CAPACITY IS '{}'",
				commonBlit.getCustomerEmail(),blitType.getSoldCount(),blitType.getCapacity());
		this.seatBlitService.checkBlitTypeSoldConditionAndSetEventDateEventStateSold(blitType);
		return commonBlit;
	}
	@Transactional
	public SeatBlit finalizeSeatBlitPayment(SeatBlit seatBlit,String refNum) {
		seatBlit.setRefNum(refNum);
		seatBlit.setCreatedAt(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()));
		seatBlit.setPaymentStatus(PaymentStatus.PAID.name());
		seatBlit.setPaymentError(ResourceUtil.getMessage(Response.PAYMENT_SUCCESS));

		seatBlit.getBlitTypeSeats()
				.stream()
				.collect(Collectors.toMap(BlitTypeSeat::getBlitType,v ->
						seatBlit.getBlitTypeSeats()
						.stream()
						.map(BlitTypeSeat::getBlitType)
						.filter(blitType -> blitType.getBlitTypeId() == v.getBlitType().getBlitTypeId())
						.count(),(count1,count2) -> count1 + count2))
				.forEach((blitType,totalCount) -> blitType.setSoldCount(blitType.getSoldCount() + totalCount.intValue()));

		seatBlit.getBlitTypeSeats().stream().map(BlitTypeSeat::getBlitType).distinct().forEach(blitType -> {
			seatBlitService.checkBlitTypeSoldConditionAndSetEventDateEventStateSold(blitType);
			log.info("****** NONE FREE SEAT BLIT SOLD COUNT RESERVED BY USER '{}' SOLD COUNT IS '{}' AND BLIT TYPE CAPACITY IS '{}'",
			seatBlit.getCustomerEmail(),blitType.getSoldCount(),blitType.getCapacity());
		});
		seatBlit.getBlitTypeSeats().forEach(blitTypeSeat -> {
			blitTypeSeat.setState(BlitTypeSeatState.SOLD.name());
			blitTypeSeat.setSoldDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()));
			blitTypeSeat.setReserveDate(null);
		});
		return seatBlit;
	}
}
