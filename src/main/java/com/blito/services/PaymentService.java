package com.blito.services;

import com.blito.enums.PaymentStatus;
import com.blito.enums.Response;
import com.blito.enums.SeatType;
import com.blito.enums.State;
import com.blito.exceptions.BlitNotAvailableException;
import com.blito.exceptions.InconsistentDataException;
import com.blito.exceptions.NotFoundException;
import com.blito.exceptions.ZarinpalException;
import com.blito.mappers.CommonBlitMapper;
import com.blito.models.Blit;
import com.blito.models.BlitType;
import com.blito.models.CommonBlit;
import com.blito.payments.saman.SamanBankService;
import com.blito.payments.zarinpal.PaymentVerificationResponse;
import com.blito.payments.zarinpal.client.ZarinpalClient;
import com.blito.repositories.BlitRepository;
import com.blito.repositories.BlitTypeRepository;
import com.blito.repositories.CommonBlitRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.services.util.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class PaymentService {
	private static final Object paymentCompletionLock = new Object();
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
	private BlitService blitService;
	@Autowired
	private CommonBlitMapper commonBlitMapper;

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
				if(!commonBlit.getBlitType().getBlitTypeState().equals(State.OPEN.name()))
					throw new BlitNotAvailableException(ResourceUtil.getMessage(Response.BLIT_NOT_AVAILABLE));
				checkBlitCapacity(commonBlit);
				PaymentVerificationResponse verificationResponse = zarinpalClient.getPaymentVerificationResponse(commonBlit.getTotalAmount().intValue(), authority);
				log.info("success in zarinpal verification response trackCode '{}' user email '{}' ref number '{}'",blit.getTrackCode(),blit.getCustomerEmail(), verificationResponse.getRefID());
				CommonBlit persistedBlit = null;
				synchronized (paymentCompletionLock) {
					log.info("User with email '{}' holding the lock after payment",commonBlit.getCustomerEmail());
					persistedBlit = persistZarinpalBoughtBlit(commonBlit, authority, String.valueOf(verificationResponse.getRefID()), ZarinpalException.generateMessage(verificationResponse.getStatus()));
					log.info("User with email '{}' released the lock after payment",commonBlit.getCustomerEmail());
				}
				blitService.sendEmailAndSmsForPurchasedBlit(commonBlitMapper.createFromEntity(persistedBlit));
				return persistedBlit;
			}
			else {
				// TODO
				return null;
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
		BlitType fethedBlitType = blitTypeRepository.findOne(blit.getBlitType().getBlitTypeId());
		if(blit.getCount() + fethedBlitType.getSoldCount() > fethedBlitType.getCapacity())
			throw new InconsistentDataException(
					ResourceUtil.getMessage(Response.REQUESTED_BLIT_COUNT_IS_MORE_THAN_CAPACITY));
	}
	@Transactional
	public CommonBlit persistZarinpalBoughtBlit(CommonBlit blit, String authority, String refNum, String paymentMessage) {
		CommonBlit commonBlit = commonBlitRepository.findOne(blit.getBlitId());
		BlitType blitType = commonBlit.getBlitType();
		commonBlit.setRefNum(refNum);
		commonBlit.setCreatedAt(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()));
		commonBlit.setPaymentStatus(PaymentStatus.PAID.name());
		commonBlit.setPaymentError(ResourceUtil.getMessage(Response.PAYMENT_SUCCESS));
		blitService.checkBlitTypeRestrictionsForBuy(blitType, commonBlit);
		blitType.setSoldCount(blitType.getSoldCount() + commonBlit.getCount());
		log.info("****** NONE FREE BLIT SOLD COUNT RESERVED BY USER '{}' SOLD COUNT IS '{}'",commonBlit.getCustomerEmail(),blitType.getSoldCount());
		blitService.checkBlitTypeSoldConditionAndSetEventDateEventStateSold(blitType);
		return commonBlit;
	}
}
