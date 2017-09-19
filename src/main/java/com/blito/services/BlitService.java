package com.blito.services;

import com.blito.enums.*;
import com.blito.exceptions.InconsistentDataException;
import com.blito.exceptions.NotAllowedException;
import com.blito.exceptions.NotFoundException;
import com.blito.exceptions.ResourceNotFoundException;
import com.blito.mappers.CommonBlitMapper;
import com.blito.models.BlitType;
import com.blito.models.CommonBlit;
import com.blito.models.Event;
import com.blito.models.User;
import com.blito.repositories.*;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.blit.CommonBlitViewModel;
import com.blito.rest.viewmodels.discount.DiscountValidationViewModel;
import com.blito.rest.viewmodels.payments.PaymentRequestViewModel;
import com.blito.search.SearchViewModel;
import com.blito.services.util.HtmlRenderer;
import io.vavr.concurrent.Future;
import io.vavr.control.Try;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class BlitService {
	private static final Object reserveFreeBlitLock = new Object();
	@Autowired
	private CommonBlitMapper commonBlitMapper;
	@Autowired
	private CommonBlitRepository commonBlitRepository;
	@Autowired
	private BlitTypeRepository blitTypeRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BlitRepository blitRepository;
	@Autowired
	private SearchService searchService;
	@Autowired
	private ExcelService excelService;
	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private DiscountService discountService;
	@Autowired
	private MailService mailService;
	@Autowired
	private SmsService smsService;
	@Autowired
	private HtmlRenderer htmlRenderer;
	@Autowired
	private PaymentRequestService paymentRequestService;

	@Value("${zarinpal.web.gateway}")
	private String zarinpalGatewayURL;

	private final Logger log = LoggerFactory.getLogger(BlitService.class);

	public Object getBlitByTrackCode(String trackCode) {
		return blitRepository.findByTrackCode(trackCode).map(blit -> {
			if (blit.getSeatType().equals(SeatType.COMMON.name())) {
				if (blit.getPaymentStatus().equals(PaymentStatus.PENDING.name()))
					return new CommonBlitViewModel(
							new ResultVm(blit.getPaymentError(), false));
				else if (blit.getPaymentStatus().equals(PaymentStatus.ERROR.name()))
					return new CommonBlitViewModel(
							new ResultVm((blit.getPaymentError() == null || blit.getPaymentError().isEmpty())
									? ResourceUtil.getMessage(Response.PAYMENT_ERROR)
									: blit.getPaymentError(), false));
				else
					return commonBlitMapper.createFromEntity(commonBlitRepository.findOne(blit.getBlitId()));
			} else
				// TODO
				throw new NotImplementedException("Seat Type blit not implemented yet");

		}).orElseThrow(() -> new ResourceNotFoundException(ResourceUtil.getMessage(Response.BLIT_NOT_FOUND)));
	}
	@Transactional
	public Object createCommonBlitAuthorized(CommonBlitViewModel vmodel,User user) {

		CommonBlit commonBlit = commonBlitMapper.createFromViewModel(vmodel);
		BlitType blitType = Optional.ofNullable(blitTypeRepository.findOne(vmodel.getBlitTypeId()))
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_TYPE_NOT_FOUND)));
		checkBlitTypeRestrictionsForBuy(blitType,commonBlit);
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
					.createFromEntity(reserveFreeBlit(blitType, commonBlit, user));
			log.info("User with email '{}' released the lock",user.getEmail());
		}
		// UNLOCK
		sendEmailAndSmsForPurchasedBlit(responseBlit);
		return responseBlit;
	}

	public void sendEmailAndSmsForPurchasedBlit(CommonBlitViewModel commonBlitViewModel) {
		Try.run(() -> {
			Map<String, Object> map = new HashMap<>();
			map.put("blit", commonBlitViewModel);
			Future.runRunnable(() -> mailService.sendEmail(commonBlitViewModel.getCustomerEmail(), htmlRenderer.renderHtml("ticket", map),
					ResourceUtil.getMessage(Response.BLIT_RECIEPT)))
					.onFailure((throwable) -> log.error("exception in sending email '{}'",throwable));
			Future.runRunnable(() -> smsService.sendBlitRecieptSms(commonBlitViewModel.getCustomerMobileNumber(), commonBlitViewModel.getTrackCode()))
					.onFailure(throwable -> log.error("exception in sending sms '{}'",throwable));
		}).onFailure(throwable -> log.error("exception in sendMailAndSmsForPurchasedBlit '{0}'",throwable));
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
							return paymentRequestService.createPurchaseRequest(blitType, commonBlit, optionalUser);
						}
					}
					else {
						throw new NotAllowedException(ResourceUtil.getMessage(Response.DISCOUNT_CODE_NOT_VALID));
					}
				}).orElseGet(() -> {
					if (commonBlit.getCount() * blitType.getPrice() != commonBlit.getTotalAmount())
						throw new InconsistentDataException(ResourceUtil.getMessage(Response.INCONSISTENT_TOTAL_AMOUNT));
					return paymentRequestService.createPurchaseRequest(blitType, commonBlit, optionalUser);
				});
	}

	@Transactional
	public PaymentRequestViewModel createCommonBlitUnauthorizedAndNoneFreeBlits(CommonBlitViewModel vmodel) {
		CommonBlit commonBlit = commonBlitMapper.createFromViewModel(vmodel);
		BlitType blitType = Optional.ofNullable(blitTypeRepository.findOne(vmodel.getBlitTypeId()))
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_TYPE_NOT_FOUND)));
		if (blitType.isFree())
			throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
		checkBlitTypeRestrictionsForBuy(blitType,commonBlit);
		return validateDiscountCodeIfPresentAndCalculateTotalAmount(vmodel,commonBlit,Optional.empty(),blitType);
	}

	// TODO: 9/6/17 Test payment scenario
	@Transactional
	public CommonBlit persistNoneFreeCommonBlit(BlitType blitType, CommonBlit commonBlit, Optional<User> optionalUser,
			String token, String trackCode) {
		BlitType attachedBlitType = blitTypeRepository.findOne(blitType.getBlitTypeId());
		optionalUser.ifPresent(user -> {
			User attachedUser = userRepository.findOne(user.getUserId());
			commonBlit.setUser(attachedUser);
		});
		commonBlit.setBlitType(attachedBlitType);
		commonBlit.setToken(token);
		commonBlit.setTrackCode(trackCode);
		commonBlit.setPaymentError(ResourceUtil.getMessage(Response.PAYMENT_PENDING));
		commonBlit.setPaymentStatus(PaymentStatus.PENDING.name());
		return commonBlitRepository.save(commonBlit);
	}

	@Transactional
	public CommonBlit reserveFreeBlit(BlitType blitType, CommonBlit commonBlit, User user) {
		User attachedUser = userRepository.findOne(user.getUserId());
		BlitType attachedBlitType = increaseSoldCount(blitType.getBlitTypeId(), commonBlit);
		log.info("****** FREE BLIT SOLD COUNT RESERVED BY USER '{}' SOLD COUNT IS '{}'", user.getEmail(),
				attachedBlitType.getSoldCount());
		commonBlit.setTrackCode(generateTrackCode());
		commonBlit.setUser(attachedUser);
		commonBlit.setBlitType(attachedBlitType);
		commonBlit.setPaymentStatus(PaymentStatus.FREE.name());
		commonBlit.setBankGateway(BankGateway.NONE.name());
		attachedUser.addBlits(commonBlit);
		return commonBlit;
	}

	private BlitType increaseSoldCount(long blitTypeId, CommonBlit commonBlit) {
		BlitType blitType = blitTypeRepository.findOne(blitTypeId);
		checkBlitTypeRestrictionsForBuy(blitType, commonBlit);
		blitType.setSoldCount(blitType.getSoldCount() + commonBlit.getCount());
		checkBlitTypeSoldConditionAndSetEventDateEventStateSold(blitType);
		return blitType;
	}

	public void checkBlitTypeSoldConditionAndSetEventDateEventStateSold(BlitType blitType) {
		if (blitType.getSoldCount() == blitType.getCapacity()) {
			blitType.setBlitTypeState(State.SOLD.name());
			if (blitType.getEventDate().getBlitTypes().stream()
					.allMatch(b -> b.getBlitTypeState().equals(State.SOLD.name()))) {
				blitType.getEventDate().setEventDateState(State.SOLD.name());
			}
			if (blitType.getEventDate().getEvent().getEventDates().stream()
					.allMatch(ed -> ed.getEventDateState().equals(State.SOLD.name()))) {
				blitType.getEventDate().getEvent().setEventState(State.SOLD.name());
				blitType.getEventDate().getEvent()
						.setEventSoldDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()));
			}
		}
	}

	void checkBlitTypeRestrictionsForBuy(BlitType blitType, CommonBlit commonBlit) {

		if (blitType.getBlitTypeState().equals(State.SOLD.name()))
			throw new NotAllowedException(ResourceUtil.getMessage(Response.BLIT_TYPE_SOLD));

		if (blitType.getBlitTypeState().equals(State.CLOSED.name()))
			throw new NotAllowedException(ResourceUtil.getMessage(Response.BLIT_TYPE_CLOSED));
		if (!blitType.getEventDate().getEvent().getEventState().equals(State.OPEN.name())) {
			throw new NotAllowedException(ResourceUtil.getMessage(Response.EVENT_NOT_OPEN));
		}
		if(!blitType.getEventDate().getEventDateState().equals(State.OPEN.name())) {
			throw new NotAllowedException(ResourceUtil.getMessage(Response.EVENT_DATE_NOT_OPEN));
		}
		if (commonBlit.getCount() + blitType.getSoldCount() > blitType.getCapacity())
			throw new InconsistentDataException(
					ResourceUtil.getMessage(Response.REQUESTED_BLIT_COUNT_IS_MORE_THAN_CAPACITY));
	}

	public String generateTrackCode() {
		String trackCode = RandomUtil.generateTrackCode();
		while (blitRepository.findByTrackCode(trackCode).isPresent()) {
			return generateTrackCode();
		}
		return trackCode;
	}

	public Page<CommonBlitViewModel> searchCommonBlits(SearchViewModel<CommonBlit> searchViewModel, Pageable pageable) {
		return searchService.search(searchViewModel, pageable, commonBlitMapper, commonBlitRepository);
	}

	public Map<String, Object> searchCommonBlitsForExcel(SearchViewModel<CommonBlit> searchViewModel) {
		Set<CommonBlitViewModel> blits = searchService.search(searchViewModel, commonBlitMapper, commonBlitRepository);
		if (blits.stream().findFirst().isPresent()) {
			CommonBlitViewModel blit = blits.stream().findFirst().get();
			if (blit.getAdditionalFields() != null && !blit.getAdditionalFields().isEmpty()) {
				CommonBlit cBlit = commonBlitRepository.findOne(blit.getBlitId());
				Event event = eventRepository.findOne(cBlit.getBlitType().getEventDate().getEvent().getEventId());
				return excelService.getBlitsExcelMap(blits, event.getAdditionalFields());
			}
		}
		return excelService.getBlitsExcelMap(blits);
	}

	public Map<String, Object> getBlitPdf(String trackCode) {
		CommonBlitViewModel blit = commonBlitMapper.createFromEntity(commonBlitRepository.findByTrackCode(trackCode)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_NOT_FOUND))));
		return excelService.blitMapForPdf(blit);
	}
}
