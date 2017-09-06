package com.blito.services;

import com.blito.enums.BankGateway;
import com.blito.enums.Response;
import com.blito.enums.State;
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
import com.blito.rest.viewmodels.payments.SamanPaymentRequestResponseViewModel;
import com.blito.rest.viewmodels.payments.ZarinpalPayRequestResponseViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.util.AsyncUtil;
import com.blito.services.util.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class PaymentRequestServiceAsync {
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
	private PaymentRequestServiceAsync paymentRequestService;
	@Autowired
	private DiscountService discountService;
	
	@Value("${zarinpal.web.gateway}")
	private String zarinpalGatewayURL;
	
	private static final Object reserveFreeBlitLock = new Object();

	private final Logger log = LoggerFactory.getLogger(PaymentRequestServiceAsync.class);
	
	public CompletableFuture<String> samanBankRequestToken(String reservationNumber, long totalAmount) {
		return samanBankService.requestToken(reservationNumber, totalAmount);
	}

	public CompletableFuture<String> zarinpalRequestToken(int amount, String email, String mobile, String description) {
		return CompletableFuture.supplyAsync(() ->
			zarinpalClient.getPaymentRequest(amount, email, mobile, description));
	}
	
	public CompletableFuture<Object> createCommonBlitAuthorized(CommonBlitViewModel vmodel) {

		CommonBlit commonBlit = commonBlitMapper.createFromViewModel(vmodel);
		BlitType blitType = Optional.ofNullable(blitTypeRepository.findOne(vmodel.getBlitTypeId()))
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_TYPE_NOT_FOUND)));
		if (!blitType.getEventDate().getEventDateState().equals(State.OPEN.name()))
			throw new NotAllowedException(ResourceUtil.getMessage(Response.EVENT_DATE_NOT_OPEN));
		if (!blitType.getEventDate().getEvent().getEventState().equals(State.OPEN.name()))
			throw new NotAllowedException(ResourceUtil.getMessage(Response.EVENT_NOT_OPEN));

		// ADDITIONAL FIELDS VALIDATION

		// if condition && instead of || ??
		// if(blitType.getEventDate().getEvent().getAdditionalFields() != null)
		// {
		// Event event = blitType.getEventDate().getEvent();
		// if(!event.getAdditionalFields().isEmpty())
		// if(vmodel.getAdditionalFields() == null)
		// throw new
		// ValidationException(ResourceUtil.getMessage(Response.ADDITIONAL_FIELDS_CANT_BE_EMPTY));
		// if(vmodel.getAdditionalFields().isEmpty())
		// throw new
		// ValidationException(ResourceUtil.getMessage(Response.ADDITIONAL_FIELDS_CANT_BE_EMPTY));
		//
		// vmodel.getAdditionalFields().forEach((k,v) -> {
		// event.getAdditionalFields().keySet().stream().filter(key -> key ==
		// k).findFirst()
		// .ifPresent(key -> {
		// if(event.getAdditionalFields().get(key).equals(Constants.FIELD_DOUBLE_TYPE))
		// {
		// try {
		// Double.parseDouble(v);
		// } catch(Exception e)
		// {
		// throw new
		// ValidationException(ResourceUtil.getMessage(Response.ERROR_FIELD_TYPE_DOUBLE));
		// }
		// }
		// else
		// if(event.getAdditionalFields().get(key).equals(Constants.FIELD_INT_TYPE))
		// {
		// try {
		// Integer.parseInt(v);
		// } catch (Exception e)
		// {
		// throw new
		// ValidationException(ResourceUtil.getMessage(Response.ERROR_FIELD_TYPE_INT));
		// }
		// }
		// });
		// });
		// }
		// ADDITIONAL FIELDS VALIDATION

		User user = userRepository.findOne(SecurityContextHolder.currentUser().getUserId());

		if (blitType.isFree()) {
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
			return CompletableFuture.completedFuture(responseBlit);
		} else {
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
								return createPurchaseRequest(blitType, commonBlit, Optional.of(user));
							}
						}
						else {
							throw new NotAllowedException(ResourceUtil.getMessage(Response.DISCOUNT_CODE_NOT_VALID));
						}
					}).orElseGet(() -> {
						if (commonBlit.getCount() * blitType.getPrice() != commonBlit.getTotalAmount())
							throw new InconsistentDataException(ResourceUtil.getMessage(Response.INCONSISTENT_TOTAL_AMOUNT));
						return createPurchaseRequest(blitType, commonBlit, Optional.of(user));
					});
		}
	}


	public CompletableFuture<Object> createCommonBlitUnauthorizedAndNoneFreeBlits(CommonBlitViewModel vmodel) {
		CommonBlit commonBlit = commonBlitMapper.createFromViewModel(vmodel);
		BlitType blitType = Optional.ofNullable(blitTypeRepository.findOne(vmodel.getBlitTypeId()))
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_TYPE_NOT_FOUND)));
		if (blitType.isFree())
			throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
		if (!blitType.getEventDate().getEventDateState().equals(State.OPEN.name()))
			throw new NotAllowedException(ResourceUtil.getMessage(Response.EVENT_DATE_NOT_OPEN));
		if (!blitType.getEventDate().getEvent().getEventState().equals(State.OPEN.name()))
			throw new NotAllowedException(ResourceUtil.getMessage(Response.EVENT_NOT_OPEN));
		if (commonBlit.getCount() * blitType.getPrice() != commonBlit.getTotalAmount())
			throw new InconsistentDataException(ResourceUtil.getMessage(Response.INCONSISTENT_TOTAL_AMOUNT));

		return createPurchaseRequest(blitType, commonBlit, Optional.empty());
	}

	private CompletableFuture<Object> createPurchaseRequest(BlitType blitType, CommonBlit commonBlit,
															Optional<User> optionalUser) {
		blitService.checkBlitTypeRestrictionsForBuy(blitType, commonBlit);
		String trackCode = blitService.generateTrackCode();
		switch (Enum.valueOf(BankGateway.class, commonBlit.getBankGateway())) {
		case ZARINPAL:
			log.debug("Before requesting token from zarinpal gateway user email '{}' and blit track code '{}'",
					commonBlit.getCustomerEmail(), trackCode);
			return paymentRequestService
					.zarinpalRequestToken(commonBlit.getTotalAmount().intValue(), commonBlit.getCustomerEmail(),
							commonBlit.getCustomerMobileNumber(), blitType.getEventDate().getEvent().getDescription())
					.thenApply(token -> {
						log.debug("Successfully get token from zarinpal gateway user email '{}' and token '{}'",
								commonBlit.getCustomerEmail(), token);
						CommonBlit persisted = blitService.persistNoneFreeCommonBlit(blitType, commonBlit, optionalUser, token,
								trackCode);
						ZarinpalPayRequestResponseViewModel zarinpalResponse = new ZarinpalPayRequestResponseViewModel();
						zarinpalResponse.setGateway(BankGateway.ZARINPAL);
						zarinpalResponse.setZarinpalWebGatewayURL(zarinpalGatewayURL + persisted.getToken());
						return zarinpalResponse;
					});
		case SAMAN:
			log.debug("Before requesting token from saman gateway user email '{}' and blit track code '{}'",
					commonBlit.getCustomerEmail(), trackCode);
			return paymentRequestService.samanBankRequestToken(trackCode, commonBlit.getTotalAmount())
					.thenApply(token -> {
						log.debug("Successfully get token from saman bank gateway user email '{}' and token '{}'",
								commonBlit.getCustomerEmail(), token);
						CommonBlit persisted = blitService.persistNoneFreeCommonBlit(blitType, commonBlit, optionalUser, token,
								trackCode);
						SamanPaymentRequestResponseViewModel samanResponse = new SamanPaymentRequestResponseViewModel();
						samanResponse.setToken(persisted.getToken());
						samanResponse.setGateway(BankGateway.SAMAN);
						samanResponse.setRedirectURL("http://localhost:8085/ws");
						return samanResponse;
					});
		default:
			throw new NotFoundException(ResourceUtil.getMessage(Response.BANK_GATEWAY_NOT_FOUND));
		}
	}
}
