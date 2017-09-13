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
import com.blito.search.SearchViewModel;
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
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
