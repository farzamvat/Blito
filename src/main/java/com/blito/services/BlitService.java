package com.blito.services;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.hibernate.exception.LockAcquisitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.blito.enums.BankGateway;
import com.blito.enums.PaymentStatus;
import com.blito.enums.Response;
import com.blito.enums.SeatType;
import com.blito.enums.State;
import com.blito.exceptions.InconsistentDataException;
import com.blito.exceptions.NotAllowedException;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.CommonBlitMapper;
import com.blito.models.BlitType;
import com.blito.models.CommonBlit;
import com.blito.models.Event;
import com.blito.models.User;
import com.blito.repositories.BlitRepository;
import com.blito.repositories.BlitTypeRepository;
import com.blito.repositories.CommonBlitRepository;
import com.blito.repositories.EventRepository;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.blit.CommonBlitViewModel;
import com.blito.rest.viewmodels.payments.SamanPaymentRequestResponseViewModel;
import com.blito.rest.viewmodels.payments.ZarinpalPayRequetsResponseViewModel;
import com.blito.search.SearchViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.util.HtmlRenderer;

@Service
public class BlitService {

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
	private HtmlRenderer htmlRenderer;
	@Autowired
	private MailService mailService;
	@Autowired
	private PaymentRequestServiceAsync paymentRequestService;
	
	private ReentrantLock lock = new ReentrantLock(true);

	@Value("${zarinpal.web.gateway}")
	private String zarinpalGatewayURL;

	private final Logger log = LoggerFactory.getLogger(BlitService.class);

	public Object getBlitByTrackCode(String trackCode) {
		return blitRepository.findByTrackCode(trackCode).map(blit -> {
			if (blit.getSeatType().equals(SeatType.COMMON.name())) {
				if (blit.getPaymentStatus().equals(PaymentStatus.PENDING.name()))
					return new CommonBlitViewModel(
							new ResultVm(ResourceUtil.getMessage(Response.PAYMENT_PENDING), false));
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

		}).orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_NOT_FOUND)));
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
			if (commonBlit.getCount() > 5)
				throw new NotAllowedException(ResourceUtil.getMessage(Response.BLIT_COUNT_EXCEEDS_LIMIT));
			if (commonBlit.getCount() + commonBlitRepository.countByCustomerEmailAndBlitTypeBlitTypeId(user.getEmail(),
					blitType.getBlitTypeId()) > 5)
				throw new NotAllowedException(ResourceUtil.getMessage(Response.BLIT_COUNT_EXCEEDS_LIMIT_TOTAL));
			// LOCK
			lock.lock();
			log.debug("User with email '{}' holding the lock",user.getEmail());
			CommonBlitViewModel responseBlit = null;
			try {
				responseBlit = commonBlitMapper
						.createFromEntity(reserveFreeBlit(blitType, commonBlit, user));
			} finally {
				lock.unlock();
				log.debug("User with email '{}' released the lock",user.getEmail());
			}
			// UNLOCK
			Map<String, Object> map = new HashMap<>();
			map.put("blit", responseBlit);
			mailService.sendEmail(responseBlit.getCustomerEmail(), htmlRenderer.renderHtml("ticket", map),
					ResourceUtil.getMessage(Response.BLIT_RECIEPT));
			return CompletableFuture.completedFuture(responseBlit);
		} else {
			if (commonBlit.getCount() * blitType.getPrice() != commonBlit.getTotalAmount())
				throw new InconsistentDataException(ResourceUtil.getMessage(Response.INCONSISTENT_TOTAL_AMOUNT));
			return buyCommonBlit(blitType, commonBlit, Optional.of(user));
		}
	}

	@Transactional
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
		return buyCommonBlit(blitType, commonBlit, Optional.empty());
	}

	private CompletableFuture<Object> buyCommonBlit(BlitType blitType, CommonBlit commonBlit,
			Optional<User> optionalUser) {
		checkBlitTypeRestrictionsForBuy(blitType, commonBlit);
		String trackCode = generateTrackCode();
		switch (Enum.valueOf(BankGateway.class, commonBlit.getBankGateway())) {
		case ZARINPAL:
			log.debug("Befor requesting token from zarinpal gateway user email '{}' and blit track code '{}'",
					commonBlit.getCustomerEmail(), trackCode);
			return paymentRequestService
					.zarinpalRequestToken((int) commonBlit.getTotalAmount(), commonBlit.getCustomerEmail(),
							commonBlit.getCustomerMobileNumber(), blitType.getEventDate().getEvent().getDescription())
					.thenApply(token -> {
						log.debug("Successfully get token from zarinpal gateway user email '{}' and token '{}'",
								commonBlit.getCustomerEmail(), token);
						CommonBlit persisted = persistNoneFreeCommonBlit(blitType, commonBlit, optionalUser, token,
								trackCode);
						ZarinpalPayRequetsResponseViewModel zarinpalResponse = new ZarinpalPayRequetsResponseViewModel();
						zarinpalResponse.setGateway(BankGateway.ZARINPAL);
						zarinpalResponse.setZarinpalWebGatewayURL(zarinpalGatewayURL + persisted.getToken());
						return zarinpalResponse;
					});
		case SAMAN:
			log.debug("Befor requesting token from saman gateway user email '{}' and blit track code '{}'",
					commonBlit.getCustomerEmail(), trackCode);
			return paymentRequestService.samanBankRequestToken(trackCode, commonBlit.getTotalAmount())
					.thenApply(token -> {
						log.debug("Successfully get token from saman bank gateway user email '{}' and token '{}'",
								commonBlit.getCustomerEmail(), token);
						CommonBlit persisted = persistNoneFreeCommonBlit(blitType, commonBlit, optionalUser, token,
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

	@Transactional/*(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)*/
	private CommonBlit persistNoneFreeCommonBlit(BlitType blitType, CommonBlit commonBlit, Optional<User> optionalUser,
			String token, String trackCode) {
		checkBlitTypeRestrictionsForBuy(blitType, commonBlit);
		BlitType attachedBlitType = blitTypeRepository.findOne(blitType.getBlitTypeId());
		optionalUser.ifPresent(user -> {
			User attachedUser = userRepository.findOne(user.getUserId());
			commonBlit.setUser(attachedUser);
		});
		commonBlit.setBlitType(attachedBlitType);
		commonBlit.setToken(token);
		commonBlit.setTrackCode(trackCode);
		commonBlit.setPaymentStatus(PaymentStatus.PENDING.name());
		return commonBlitRepository.save(commonBlit);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private CommonBlit reserveFreeBlit(BlitType blitType, CommonBlit commonBlit, User user) {
		User attachedUser = userRepository.findOne(user.getUserId());
		BlitType attachedBlitType = increaseSoldCount(blitType.getBlitTypeId(), commonBlit);
		log.info("****** FREE BLIT SOLD COUNT RESERVED BY USER '{}' SOLD COUNT IS '{}'",user.getEmail(),attachedBlitType.getSoldCount());
		commonBlit.setTrackCode(generateTrackCode());
		commonBlit.setUser(attachedUser);
		commonBlit.setBlitType(attachedBlitType);
		commonBlit.setPaymentStatus(PaymentStatus.FREE.name());
		commonBlit.setBankGateway(BankGateway.NONE.name());
		attachedUser.addBlits(commonBlit);
		return commonBlitRepository.save(commonBlit);
	}
	
	@Transactional(propagation= Propagation.REQUIRES_NEW, isolation=Isolation.READ_COMMITTED)
	private BlitType increaseSoldCount(long blitTypeId,CommonBlit commonBlit)
	{
		BlitType blitType = blitTypeRepository.findByBlitTypeId(blitTypeId);
		blitType.setSoldCount(blitType.getSoldCount() + commonBlit.getCount());
		checkBlitTypeRestrictionsForBuy(blitType, commonBlit);
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
		return blitType;
	}

	private void checkBlitTypeRestrictionsForBuy(BlitType blitType, CommonBlit commonBlit) {

		if (blitType.getBlitTypeState().equals(State.SOLD.name()))
			throw new NotAllowedException(ResourceUtil.getMessage(Response.BLIT_TYPE_SOLD));

		if (blitType.getBlitTypeState().equals(State.CLOSED.name()))
			throw new NotAllowedException(ResourceUtil.getMessage(Response.BLIT_TYPE_CLOSED));
		if (!blitType.getEventDate().getEvent().getEventState().equals(State.OPEN.name())) {
			throw new NotAllowedException(ResourceUtil.getMessage(Response.EVENT_NOT_OPEN));
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

	public Map<String, Object> getExcel() {
		return excelService
				.getBlitsExcelMap(commonBlitMapper.createFromEntities(new HashSet<>(commonBlitRepository.findAll())));
	}
}
