package com.blito.services;

import com.blito.enums.BankGateway;
import com.blito.enums.Response;
import com.blito.exceptions.InconsistentDataException;
import com.blito.exceptions.NotAllowedException;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.CommonBlitMapper;
import com.blito.models.BlitType;
import com.blito.models.CommonBlit;
import com.blito.models.User;
import com.blito.payments.saman.SamanBankService;
import com.blito.payments.zarinpal.client.ZarinpalClient;
import com.blito.repositories.BlitTypeRepository;
import com.blito.repositories.CommonBlitRepository;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.blit.CommonBlitViewModel;
import com.blito.rest.viewmodels.discount.DiscountValidationViewModel;
import com.blito.rest.viewmodels.payments.PaymentRequestViewModel;
import com.blito.rest.viewmodels.payments.ZarinpalPayRequestResponseViewModel;
import com.blito.services.util.AsyncUtil;
import com.blito.services.util.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class PaymentRequestService {
	@Autowired
	private SamanBankService samanBankService;
	@Autowired
	private ZarinpalClient zarinpalClient;
	@Autowired
	private BlitTypeRepository blitTypeRepository;
	@Autowired
	private MailService mailService;
	@Autowired
	private SmsService smsService;
	@Autowired
	private CommonBlitMapper commonBlitMapper;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BlitService blitService;
	@Autowired
	private HtmlRenderer htmlRenderer;
	@Autowired
	private CommonBlitRepository commonBlitRepository;

	@Autowired
	private DiscountService discountService;
	
	@Value("${zarinpal.web.gateway}")
	private String zarinpalGatewayURL;
	
	private static final Object reserveFreeBlitLock = new Object();

	private final Logger log = LoggerFactory.getLogger(PaymentRequestService.class);
	
	public CompletableFuture<String> samanBankRequestToken(String reservationNumber, long totalAmount) {
		return samanBankService.requestToken(reservationNumber, totalAmount);
	}

	public String zarinpalRequestToken(int amount, String email, String mobile, String description) {
		return zarinpalClient.getPaymentRequest(amount, email, mobile, description);
	}

	@Transactional
	public Object createCommonBlitAuthorized(CommonBlitViewModel vmodel,User user) {

		CommonBlit commonBlit = commonBlitMapper.createFromViewModel(vmodel);
		BlitType blitType = Optional.ofNullable(blitTypeRepository.findOne(vmodel.getBlitTypeId()))
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_TYPE_NOT_FOUND)));

		blitService.checkBlitTypeRestrictionsForBuy(blitType,commonBlit);

		if (blitType.isFree()) {
			return reserveFreeCommonBlitForAuthorizedUser(blitType,commonBlit,user);
		} else {
			return validateDiscountCodeIfPresentAndCalculateTotalAmount(vmodel,commonBlit,Optional.of(user),blitType);
		}
	}

	@Transactional
	CommonBlitViewModel reserveFreeCommonBlitForAuthorizedUser(BlitType blitType,CommonBlit commonBlit,User user) {
		if (commonBlit.getCount() > 10)
			throw new NotAllowedException(ResourceUtil.getMessage(Response.BLIT_COUNT_EXCEEDS_LIMIT));
		if (commonBlit.getCount() + commonBlitRepository.countByCustomerEmailAndBlitTypeBlitTypeId(user.getEmail(),
				blitType.getBlitTypeId()) > 10)
			throw new NotAllowedException(ResourceUtil.getMessage(Response.BLIT_COUNT_EXCEEDS_LIMIT_TOTAL));
		final CommonBlitViewModel responseBlit;
		// LOCK
		synchronized (reserveFreeBlitLock) {
			log.info("User with email '{}' holding the lock",user.getEmail());
			responseBlit = commonBlitMapper
					.createFromEntity(blitService.reserveFreeBlit(blitType, commonBlit, user));
			log.info("User with email '{}' released the lock",user.getEmail());
		}
		// UNLOCK
		Map<String, Object> map = new HashMap<>();
		map.put("blit", responseBlit);

		AsyncUtil.run(() -> mailService.sendEmail(responseBlit.getCustomerEmail(), htmlRenderer.renderHtml("ticket", map),
				ResourceUtil.getMessage(Response.BLIT_RECIEPT)));
		AsyncUtil.run(() -> smsService.sendBlitRecieptSms(responseBlit.getCustomerMobileNumber(), responseBlit.getTrackCode()));
		return responseBlit;
	}

	@Transactional
	PaymentRequestViewModel validateDiscountCodeIfPresentAndCalculateTotalAmount(CommonBlitViewModel vmodel, CommonBlit commonBlit, Optional<User> optionalUser, BlitType blitType) {
		return Optional.ofNullable(vmodel.getDiscountCode())
				.filter(code -> !code.isEmpty())
				.map(code -> {
					DiscountValidationViewModel discountValidationViewModel = discountService.validateDiscountCodeBeforePurchaseRequest(vmodel.getBlitTypeId(),code,vmodel.getCount());
					if(discountValidationViewModel.isValid()) {
						if(!discountValidationViewModel.getTotalAmount().equals(commonBlit.getTotalAmount())) {
							throw new NotAllowedException(ResourceUtil.getMessage(Response.DISCOUNT_CODE_NOT_VALID));
						} else if (commonBlit.getCount() * blitType.getPrice() != commonBlit.getPrimaryAmount()) {
							throw new InconsistentDataException(ResourceUtil.getMessage(Response.INCONSISTENT_TOTAL_AMOUNT));
						} else {
							return createPurchaseRequest(blitType, commonBlit, optionalUser);
						}
					}
					else {
						throw new NotAllowedException(ResourceUtil.getMessage(Response.DISCOUNT_CODE_NOT_VALID));
					}
				}).orElseGet(() -> {
					if (commonBlit.getCount() * blitType.getPrice() != commonBlit.getTotalAmount())
						throw new InconsistentDataException(ResourceUtil.getMessage(Response.INCONSISTENT_TOTAL_AMOUNT));
					return createPurchaseRequest(blitType, commonBlit, optionalUser);
				});
	}

	@Transactional
	public PaymentRequestViewModel createCommonBlitUnauthorizedAndNoneFreeBlits(CommonBlitViewModel vmodel) {
		CommonBlit commonBlit = commonBlitMapper.createFromViewModel(vmodel);
		BlitType blitType = Optional.ofNullable(blitTypeRepository.findOne(vmodel.getBlitTypeId()))
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_TYPE_NOT_FOUND)));
		if (blitType.isFree())
			throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
		blitService.checkBlitTypeRestrictionsForBuy(blitType,commonBlit);
		return validateDiscountCodeIfPresentAndCalculateTotalAmount(vmodel,commonBlit,Optional.empty(),blitType);
	}

	@Transactional
	PaymentRequestViewModel createPurchaseRequest(BlitType blitType, CommonBlit commonBlit,
														  Optional<User> optionalUser) {
		blitService.checkBlitTypeRestrictionsForBuy(blitType, commonBlit);
		String trackCode = blitService.generateTrackCode();
		switch (Enum.valueOf(BankGateway.class, commonBlit.getBankGateway())) {
		case ZARINPAL:
			log.debug("Before requesting token from zarinpal gateway user email '{}' and blit track code '{}'",
					commonBlit.getCustomerEmail(), trackCode);
			String zarinpalAuthorityToken = zarinpalRequestToken(commonBlit.getTotalAmount().intValue(), commonBlit.getCustomerEmail(),
					commonBlit.getCustomerMobileNumber(), blitType.getEventDate().getEvent().getDescription());
			CommonBlit persisted = blitService.persistNoneFreeCommonBlit(blitType, commonBlit, optionalUser, zarinpalAuthorityToken,
					trackCode);
			ZarinpalPayRequestResponseViewModel zarinpalResponse = new ZarinpalPayRequestResponseViewModel();
			zarinpalResponse.setGateway(BankGateway.ZARINPAL);
			zarinpalResponse.setZarinpalWebGatewayURL(zarinpalGatewayURL + persisted.getToken());
			return zarinpalResponse;
		default:
			throw new NotFoundException(ResourceUtil.getMessage(Response.BANK_GATEWAY_NOT_FOUND));
		}
	}
}
