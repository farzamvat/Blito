package com.blito.services;

import com.blito.enums.*;
import com.blito.exceptions.*;
import com.blito.mappers.CommonBlitMapper;
import com.blito.mappers.SeatBlitMapper;
import com.blito.models.*;
import com.blito.repositories.*;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.blit.AbstractBlitViewModel;
import com.blito.rest.viewmodels.blit.CommonBlitViewModel;
import com.blito.rest.viewmodels.blit.SeatBlitViewModel;
import com.blito.rest.viewmodels.blit.SeatErrorViewModel;
import com.blito.rest.viewmodels.discount.DiscountValidationViewModel;
import com.blito.rest.viewmodels.payments.PaymentRequestViewModel;
import com.blito.search.SearchViewModel;
import com.blito.services.util.HtmlRenderer;
import io.vavr.concurrent.Future;
import io.vavr.control.Option;
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
import java.util.stream.Collectors;

@Service
public class BlitService {
	private static final Object reserveFreeCommonBlitLock = new Object();
	private static final Object reserveFreeSeatBlitLock = new Object();
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
	@Autowired
	private SeatBlitMapper seatBlitMapper;
	@Autowired
	private BlitTypeSeatRepository blitTypeSeatRepository;
	@Autowired
	private SeatBlitRepository seatBlitRepository;

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
		CommonBlit blit = commonBlitMapper.createFromViewModel((CommonBlitViewModel) vmodel);

		BlitType blitType = Optional.ofNullable(blitTypeRepository.findOne(vmodel.getBlitTypeId()))
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_TYPE_NOT_FOUND)));
		checkBlitTypeRestrictionsForBuy(blitType,blit);
		validateAdditionalFields(blitType.getEventDate().getEvent(),blit);
		if (blitType.isFree()) {
			return reserveFreeCommonBlitForAuthorizedUser(blitType,blit,user);
		} else {
			validateDiscountCodeIfPresentAndCalculateTotalAmount(vmodel,blit,blitType);
			blit.setTrackCode(generateTrackCode());
			return Option.of(paymentRequestService.createPurchaseRequest(blit))
					.map(token -> persistNoneFreeCommonBlit(blitType,blit,Optional.of(user),token))
					.map(commonBlit -> paymentRequestService.createZarinpalResponse(commonBlit.getToken()))
					.getOrElseThrow(() -> new RuntimeException("Never Happens"));
		}
	}

	@Transactional
	public Object createSeatBlitAuthorized(SeatBlitViewModel viewModel,User user) {
		SeatBlit seatBlit = seatBlitMapper.createFromViewModel(viewModel);
		BlitType blitType = Optional.ofNullable(blitTypeRepository.findOne(viewModel.getBlitTypeId()))
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_TYPE_NOT_FOUND)));;
		checkBlitTypeRestrictionsForBuy(blitType,seatBlit);
		Set<BlitTypeSeat> blitTypeSeats =
				blitTypeSeatRepository.findBySeatSeatUidInAndBlitTypeEventDateEventDateId(viewModel.getSeatUids(),viewModel.getEventDateId());
		validateSeatBlitForBuy(blitTypeSeats);
		seatBlit.setBlitTypeSeats(blitTypeSeats);
		if(blitType.isFree()) {
			return reserveFreeSeatBlitForAuthorizedUser(blitType,seatBlit,user);
		} else {
			validateDiscountCodeIfPresentAndCalculateTotalAmount(viewModel,seatBlit,blitType);
			seatBlit.setTrackCode(generateTrackCode());
			return Option.of(paymentRequestService.createPurchaseRequest(seatBlit))
					.map(token -> persistNoneFreeSeatBlit(blitType,seatBlit,Optional.of(user),token))
					.map(blit -> paymentRequestService.createZarinpalResponse(blit.getToken()))
					.getOrElseThrow(() -> new RuntimeException("Never Happens"));
		}
	}

	private void validateSeatBlitForBuy(Set<BlitTypeSeat> blitTypeSeats) {

		blitTypeSeats.stream()
				.filter(blitTypeSeat -> blitTypeSeat.getState().equals(BlitTypeSeatState.RESERVED.name()))
				.forEach(blitTypeSeat ->
					Optional.ofNullable(blitTypeSeat.getReserveDate())
							.filter(reservedDate -> reservedDate.before(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusMinutes(10L).toInstant())))
							.ifPresent(dump -> {
								blitTypeSeat.setState(BlitTypeSeatState.AVAILABLE.name());
								blitTypeSeat.setReserveDate(null);
							}));
		Optional.ofNullable(blitTypeSeats
				.stream()
				.filter(blitTypeSeat -> !blitTypeSeat.getState().equals(BlitTypeSeatState.AVAILABLE.name()))
				.map(blitTypeSeat -> new SeatErrorViewModel(blitTypeSeat.getSeat().getSeatUid(),blitTypeSeat.getState()))
				.collect(Collectors.toSet()))
				.filter(set -> !set.isEmpty())
				.ifPresent(seatErrorViewModels -> {
					throw new SeatException("seat error",seatErrorViewModels);
				});

	}

	@Transactional
	void validateAdditionalFields(Event event,Blit blit) {
		if(event.getAdditionalFields() != null && !event.getAdditionalFields().isEmpty()) {
			if(blit.getAdditionalFields().isEmpty())
				throw new AdditionalFieldsValidationException(ResourceUtil.getMessage(Response.ADDITIONAL_FIELDS_CANT_BE_EMPTY));
			else if(blit.getAdditionalFields().size() != event.getAdditionalFields().size())
				throw new AdditionalFieldsValidationException(ResourceUtil.getMessage(Response.ADDITIONAL_FIELDS_VALIDATION_ERROR));
			else if (!blit.getAdditionalFields().keySet().stream().allMatch(key -> event.getAdditionalFields().keySet().contains(key)))
				throw new AdditionalFieldsValidationException(ResourceUtil.getMessage(Response.ADDITIONAL_FIELDS_VALIDATION_ERROR));
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
		synchronized (reserveFreeCommonBlitLock) {
			log.info("User with email '{}' holding the lock",user.getEmail());
			responseBlit = commonBlitMapper
					.createFromEntity(reserveFreeBlit(blitType, commonBlit, user));
			log.info("User with email '{}' released the lock",user.getEmail());
		}
		// UNLOCK
		sendEmailAndSmsForPurchasedBlit(responseBlit);
		return responseBlit;
	}

	@Transactional
	SeatBlitViewModel reserveFreeSeatBlitForAuthorizedUser(BlitType blitType,SeatBlit seatBlit, User user) {
		if(seatBlit.getCount() > 10) {
			throw new NotAllowedException(ResourceUtil.getMessage(Response.BLIT_COUNT_EXCEEDS_LIMIT));
		}
		if(seatBlit.getCount() +
				blitTypeSeatRepository.countByBlitTypeBlitTypeIdAndSeatBlitCustomerEmail(blitType.getBlitTypeId(),seatBlit.getCustomerEmail())
				> 10) {
			throw new NotAllowedException(ResourceUtil.getMessage(Response.BLIT_COUNT_EXCEEDS_LIMIT_TOTAL));
		}
		final SeatBlitViewModel responseBlit;
		// LOCK
		synchronized (reserveFreeSeatBlitLock) {
			log.info("User with email '{}' holding the lock",user.getEmail());
			responseBlit = seatBlitMapper
					.createFromEntity(reserveFreeSeatBlit(blitType,seatBlit,user));
			log.info("User with email '{}' released the lock",user.getEmail());
		}
		// UNLOCK
//		sendEmailAndSmsForPurchasedBlit(responseBlit);
		return responseBlit;
	}

	public void sendEmailAndSmsForPurchasedBlit(CommonBlitViewModel commonBlitViewModel) {
		Map<String, Object> map = new HashMap<>();
		map.put("blit", commonBlitViewModel);
		Future.runRunnable(() -> mailService.sendEmail(commonBlitViewModel.getCustomerEmail(), htmlRenderer.renderHtml("ticket", map),
				ResourceUtil.getMessage(Response.BLIT_RECIEPT)))
				.onFailure((throwable) -> log.debug("exception in sending email '{}'",throwable));
		Future.runRunnable(() -> smsService.sendBlitRecieptSms(commonBlitViewModel.getCustomerMobileNumber(), commonBlitViewModel.getTrackCode()))
				.onFailure(throwable -> log.debug("exception in sending sms '{}'",throwable));
	}

	<T extends AbstractBlitViewModel> void validateDiscountCodeIfPresentAndCalculateTotalAmount(T vmodel, Blit blit, BlitType blitType) {
		Option.of(vmodel.getDiscountCode())
			.filter(code -> !code.isEmpty())
			.peek(code -> {
				DiscountValidationViewModel discountValidationViewModel = discountService.validateDiscountCodeBeforePurchaseRequest(vmodel.getBlitTypeId(),code,vmodel.getCount());
				if(discountValidationViewModel.isValid()) {
					if(!discountValidationViewModel.getTotalAmount().equals(blit.getTotalAmount())) {
						throw new NotAllowedException(ResourceUtil.getMessage(Response.DISCOUNT_CODE_NOT_VALID));
					} else if (blit.getCount() * blitType.getPrice() != blit.getPrimaryAmount()) {
						throw new InconsistentDataException(ResourceUtil.getMessage(Response.INCONSISTENT_TOTAL_AMOUNT));
					}
				}
				else {
					throw new NotAllowedException(ResourceUtil.getMessage(Response.DISCOUNT_CODE_NOT_VALID));
				}
			}).onEmpty(() -> {
				if (blit.getCount() * blitType.getPrice() != blit.getTotalAmount())
					throw new InconsistentDataException(ResourceUtil.getMessage(Response.INCONSISTENT_TOTAL_AMOUNT));
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
		validateAdditionalFields(blitType.getEventDate().getEvent(),commonBlit);
		validateDiscountCodeIfPresentAndCalculateTotalAmount(vmodel,commonBlit,blitType);
		commonBlit.setTrackCode(generateTrackCode());
		return Option.of(paymentRequestService.createPurchaseRequest(commonBlit))
				.map(token -> persistNoneFreeCommonBlit(blitType,commonBlit,Optional.empty(),token))
				.map(blit -> paymentRequestService.createZarinpalResponse(blit.getToken()))
				.getOrElseThrow(() -> new RuntimeException("Never Happens"));
	}

	// TODO: 9/6/17 Test payment scenario
	@Transactional
	public CommonBlit persistNoneFreeCommonBlit(BlitType blitType, CommonBlit commonBlit, Optional<User> optionalUser,
			String token) {
		BlitType attachedBlitType = blitTypeRepository.findOne(blitType.getBlitTypeId());
		optionalUser.ifPresent(user -> {
			User attachedUser = userRepository.findOne(user.getUserId());
			commonBlit.setUser(attachedUser);
		});
		commonBlit.setBlitType(attachedBlitType);
		commonBlit.setToken(token);
		commonBlit.setPaymentError(ResourceUtil.getMessage(Response.PAYMENT_PENDING));
		commonBlit.setPaymentStatus(PaymentStatus.PENDING.name());
		return commonBlitRepository.save(commonBlit);
	}

	@Transactional
	public SeatBlit persistNoneFreeSeatBlit(BlitType blitType, SeatBlit seatBlit, Optional<User> optionalUser,String token) {
		BlitType attachedBlitType = blitTypeRepository.findOne(blitType.getBlitTypeId());
		optionalUser.ifPresent(user -> {
			User attachedUser = userRepository.findOne(user.getUserId());
			seatBlit.setUser(attachedUser);
		});
		seatBlit.setToken(token);
		seatBlit.setPaymentError(ResourceUtil.getMessage(Response.PAYMENT_PENDING));
		seatBlit.setPaymentStatus(PaymentStatus.PENDING.name());
		seatBlit.getBlitTypeSeats().forEach(blitTypeSeat -> {
			blitTypeSeat.setState(BlitTypeSeatState.RESERVED.name());
			blitTypeSeat.setReserveDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()));
		});
		return seatBlitRepository.save(seatBlit);
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
		commonBlit.setTotalAmount(0L);
		commonBlit.setPrimaryAmount(0L);
		commonBlit.setPaymentStatus(PaymentStatus.FREE.name());
		commonBlit.setBankGateway(BankGateway.NONE.name());
		attachedUser.addBlits(commonBlit);
		return commonBlit;
	}

	@Transactional
	public SeatBlit reserveFreeSeatBlit(BlitType blitType, SeatBlit seatBlit, User user) {
		User attachedUser = userRepository.findOne(user.getUserId());
		BlitType attachedBlitType = increaseSoldCount(blitType.getBlitTypeId(),seatBlit);
		log.info("****** FREE SEAT BLIT SOLD COUNT RESERVED BY USER '{}' SOLD COUNT IS '{}'", user.getEmail(),
				attachedBlitType.getSoldCount());
		seatBlit.setTrackCode(generateTrackCode());
		seatBlit.setUser(attachedUser);
		seatBlit.setTotalAmount(0L);
		seatBlit.setPrimaryAmount(0L);
		seatBlit.setBankGateway(PaymentStatus.FREE.name());
		seatBlit.setBankGateway(BankGateway.NONE.name());
		seatBlit.getBlitTypeSeats().forEach(blitTypeSeat -> {
			blitTypeSeat.setState(BlitTypeSeatState.SOLD.name());
			blitTypeSeat.setSoldDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()));
		});
		attachedUser.addBlits(seatBlit);
		return seatBlit;
	}

	private BlitType increaseSoldCount(long blitTypeId, Blit blit) {
		BlitType blitType = blitTypeRepository.findOne(blitTypeId);
		blitType.setSoldCount(blitType.getSoldCount() + blit.getCount());
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

	void checkBlitTypeRestrictionsForBuy(BlitType blitType, Blit blit) {

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
		if (blit.getCount() + blitType.getSoldCount() > blitType.getCapacity())
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

	@Transactional
	public Map<String, Object> searchCommonBlitsForExcel(SearchViewModel<CommonBlit> searchViewModel) {
		Set<CommonBlit> blits = searchService.search(searchViewModel, commonBlitRepository);
		if(blits.isEmpty())
			throw new NotFoundException(ResourceUtil.getMessage(Response.SEARCH_UNSUCCESSFUL));
		CommonBlit blit = blits.stream().findAny().get();
		if (blit.getAdditionalFields() != null && !blit.getAdditionalFields().isEmpty()) {
			return excelService.getBlitsExcelMap(commonBlitMapper.createFromEntities(blits), blit.getBlitType().getEventDate().getEvent().getAdditionalFields());
		}
		return excelService.getBlitsExcelMap(commonBlitMapper.createFromEntities(blits));
	}

	public Map<String, Object> getBlitPdf(String trackCode) {
		CommonBlitViewModel blit = commonBlitMapper.createFromEntity(commonBlitRepository.findByTrackCode(trackCode)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_NOT_FOUND))));
		return excelService.blitMapForPdf(blit);
	}
}
