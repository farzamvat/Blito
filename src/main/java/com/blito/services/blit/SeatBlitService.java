package com.blito.services.blit;

import com.blito.configs.Constants;
import com.blito.enums.*;
import com.blito.exceptions.InconsistentDataException;
import com.blito.exceptions.NotAllowedException;
import com.blito.exceptions.NotFoundException;
import com.blito.exceptions.SeatException;
import com.blito.models.*;
import com.blito.repositories.BlitTypeSeatRepository;
import com.blito.repositories.EventDateRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.blit.ReservedBlitViewModel;
import com.blito.rest.viewmodels.blit.SeatBlitViewModel;
import com.blito.rest.viewmodels.blit.SeatErrorViewModel;
import com.blito.rest.viewmodels.discount.SeatBlitDiscountValidationViewModel;
import com.blito.rest.viewmodels.payments.PaymentRequestViewModel;
import com.blito.search.SearchViewModel;
import com.blito.services.ExcelService;
import com.blito.services.SalonService;
import com.blito.services.SearchService;
import io.vavr.control.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Farzam Vatanzadeh
 * 10/17/17
 * Mailto : farzam.vat@gmail.com
 **/

@Service
public class SeatBlitService extends AbstractBlitService<SeatBlit,SeatBlitViewModel> {
    private final Logger log = LoggerFactory.getLogger(SeatBlitService.class);
    private static final Object reserveSeatBlitLock = new Object();


    private BlitTypeSeatRepository blitTypeSeatRepository;
    private SalonService salonService;
    private SearchService searchService;
    private ExcelService excelService;

    private EventDateRepository eventDateRepository;

    @Autowired
    public void setSalonService(SalonService salonService) {
        this.salonService = salonService;
    }
    @Autowired
    public void setBlitTypeSeatRepository(BlitTypeSeatRepository blitTypeSeatRepository) {
        this.blitTypeSeatRepository = blitTypeSeatRepository;
    }
    @Autowired
    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }
    @Autowired
    public void setExcelService(ExcelService excelService) {
        this.excelService = excelService;
    }

    @Autowired
    public void setEventDateRepository(EventDateRepository eventDateRepository) {
        this.eventDateRepository = eventDateRepository;
    }

    private void validateSeatBlitForBuy(Set<BlitTypeSeat> blitTypeSeats) {

        if(blitTypeSeats.isEmpty()) {
            throw new SeatException(ResourceUtil.getMessage(Response.NO_SEATS_PICKED_ERROR));
        }
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
                    throw new SeatException(ResourceUtil.getMessage(Response.SELECTED_SEAT_IS_SOLD_OR_RESERVED)
                            ,seatErrorViewModels);
                });
    }

    @Transactional
    @Override
    public Map<String, Object> searchBlitsForExcel(SearchViewModel<SeatBlit> searchViewModel) {
        Set<SeatBlit> blits = searchService.search(searchViewModel, seatBlitRepository);
        if(blits.isEmpty())
            throw new NotFoundException(ResourceUtil.getMessage(Response.SEARCH_UNSUCCESSFUL));
        SeatBlit blit = blits.stream().findAny().get();
        if (blit.getAdditionalFields() != null && !blit.getAdditionalFields().isEmpty()) {
            return blit.getBlitTypeSeats().stream().findAny().map(BlitTypeSeat::getBlitType).map(BlitType::getEventDate).map(EventDate::getEvent).map(Event::getAdditionalFields)
                    .map(additionalFields -> excelService.getBlitsExcelMap(seatBlitMapper.createFromEntities(blits),additionalFields))
                    .orElseThrow(() -> new RuntimeException("Never happens"));
        }
        return excelService.getBlitsExcelMap(seatBlitMapper.createFromEntities(blits));
    }

    @Transactional
    @Override
    public Page<SeatBlitViewModel> searchBlits(SearchViewModel<SeatBlit> searchViewModel, Pageable pageable) {
        return searchService.search(searchViewModel,pageable,seatBlitMapper,seatBlitRepository);
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Override
    public Object createBlitAuthorized(SeatBlitViewModel viewModel, User user) {
        SeatBlit seatBlit = seatBlitMapper.createFromViewModel(viewModel);

        Set<BlitTypeSeat> blitTypeSeats = blitTypeSeatRepository.findBySeatSeatUidInAndBlitTypeEventDateEventDateId(viewModel.getSeatUids(),viewModel.getEventDateId());
        blitTypeSeats.stream().map(BlitTypeSeat::getBlitType).distinct().forEach(this::checkBlitTypeRestrictionsForBuy);
        EventDate eventDate = blitTypeSeats.stream().findAny().map(BlitTypeSeat::getBlitType).map(BlitType::getEventDate)
                .orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_DATE_NOT_FOUND)));
        validateAdditionalFields(eventDate.getEvent(),seatBlit);
        synchronized (reserveSeatBlitLock) {
            log.info("User with email '{}' hold reserveSeatBlitLock",user.getEmail());
            validateSeatBlitForBuy(blitTypeSeats);
            blitTypeSeats.forEach(blitTypeSeat -> {
                blitTypeSeat.setState(BlitTypeSeatState.RESERVED.name());
                blitTypeSeat.setReserveDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()));
                seatBlit.setSeats((seatBlit.getSeats() == null ? "" : seatBlit.getSeats() + " /") +
                        String.format(ResourceUtil.getMessage(Response.SEAT_INFORMATION),
                                blitTypeSeat.getSeat().getSectionName(),
                                blitTypeSeat.getSeat().getRowName(),
                                blitTypeSeat.getSeat().getSeatName()));
            });
        }
        log.info("User with email '{}' released reserveSeatBlitLock",user.getEmail());
        salonService.validateNoIndividualSeat(salonService.populateSeatInformationInSalonSchemaByEventDateId(eventDate.getEventDateId()).getSchema());
        seatBlit.setBlitTypeSeats(blitTypeSeats);
        return blitPurchaseAuthorizedSeatBlit(viewModel,user,seatBlit);
    }

    private Object blitPurchaseAuthorizedSeatBlit(SeatBlitViewModel viewModel,User user,SeatBlit seatBlit) {
        if (seatBlit.getBlitTypeSeats().stream().map(BlitTypeSeat::getBlitType).allMatch(BlitType::isFree)) {
            return reserveFreeBlitForAuthorizedUser(viewModel,seatBlit,user);
        } else {
            validateDiscountCodeIfPresentAndCalculateTotalAmount(viewModel,seatBlit);
            seatBlit.setTrackCode(generateTrackCode());
            return Option.of(paymentRequestService.createPurchaseRequest(seatBlit))
                    .map(token -> persistBlit(seatBlit,Optional.of(user),token))
                    .map(b -> paymentRequestService.createZarinpalResponse(b.getToken()))
                    .getOrElseThrow(() -> new RuntimeException("Never Happerns"));
        }
    }

    @Transactional
    public SeatBlitViewModel reserveFreeBlitForAuthorizedUser(SeatBlitViewModel seatBlitViewModel, SeatBlit seatBlit, User user) {
        if(seatBlit.getCount() > 10) {
            throw new NotAllowedException(ResourceUtil.getMessage(Response.BLIT_COUNT_EXCEEDS_LIMIT));
        }
        if(seatBlit.getCount() +
                blitTypeSeatRepository.countByBlitTypeEventDateEventDateIdAndSeatBlitCustomerEmail(seatBlitViewModel.getEventDateId(),seatBlit.getCustomerEmail())
                > 10) {
            throw new NotAllowedException(ResourceUtil.getMessage(Response.BLIT_COUNT_EXCEEDS_LIMIT_TOTAL));
        }

        final SeatBlitViewModel responseBlit = seatBlitMapper
                .createFromEntity(reserveFreeBlit(seatBlit,user));
        log.info("User with email '{}' reserved free seat blit",user.getEmail());

		sendEmailAndSmsForPurchasedBlit(responseBlit);
        return responseBlit;
    }

    private void validateDiscountCodeIfPresentAndCalculateTotalAmount(SeatBlitViewModel seatBlitViewModel,SeatBlit seatBlit) {
        Option.of(seatBlit.getDiscountCode())
                .filter(code -> !code.isEmpty())
                .peek(code -> {
                    SeatBlitDiscountValidationViewModel discountValidationViewModel =
                            discountService.validateDiscountCodeBeforePurchaseRequestForSeatBlit(seatBlitViewModel);
                    if(discountValidationViewModel.getValid()) {
                        if(!discountValidationViewModel.getTotalAmount().equals(seatBlit.getTotalAmount())) {
                            throw new NotAllowedException(ResourceUtil.getMessage(Response.DISCOUNT_CODE_NOT_VALID));
                        } else if(seatBlit.getBlitTypeSeats().stream().map(BlitTypeSeat::getBlitType).mapToLong(BlitType::getPrice).sum() != seatBlit.getPrimaryAmount()) {
                            throw new InconsistentDataException(ResourceUtil.getMessage(Response.INCONSISTENT_TOTAL_AMOUNT));
                        }
                    } else {
                        throw new NotAllowedException(ResourceUtil.getMessage(Response.DISCOUNT_CODE_NOT_VALID));
                    }
                }).onEmpty(() -> {
                    seatBlit.setPrimaryAmount(seatBlit.getTotalAmount());
                    if(seatBlit.getBlitTypeSeats().stream().map(BlitTypeSeat::getBlitType).mapToLong(BlitType::getPrice).sum() != seatBlit.getTotalAmount()) {
                        throw new InconsistentDataException(ResourceUtil.getMessage(Response.INCONSISTENT_TOTAL_AMOUNT));
                    }
                });
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Override
    public PaymentRequestViewModel createUnauthorizedAndNoneFreeBlits(SeatBlitViewModel viewModel) {
        SeatBlit seatBlit = seatBlitMapper.createFromViewModel(viewModel);

        Set<BlitTypeSeat> blitTypeSeats = blitTypeSeatRepository.findBySeatSeatUidInAndBlitTypeEventDateEventDateId(viewModel.getSeatUids(),viewModel.getEventDateId());
        blitTypeSeats.stream().map(BlitTypeSeat::getBlitType).distinct().forEach(this::checkBlitTypeRestrictionsForBuy);
        if(blitTypeSeats.stream().map(BlitTypeSeat::getBlitType).anyMatch(BlitType::isFree)) {
            throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
        }
        EventDate eventDate = blitTypeSeats.stream().findAny().map(BlitTypeSeat::getBlitType).map(BlitType::getEventDate)
                .orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_DATE_NOT_FOUND)));
        validateAdditionalFields(eventDate.getEvent(),seatBlit);

        synchronized (reserveSeatBlitLock) {
            log.info("unauthorized user with email '{}' hold reserveSeatBlitLock",viewModel.getCustomerEmail());
            validateSeatBlitForBuy(blitTypeSeats);
            blitTypeSeats.forEach(blitTypeSeat -> {
                blitTypeSeat.setState(BlitTypeSeatState.RESERVED.name());
                blitTypeSeat.setReserveDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()));
                seatBlit.setSeats((seatBlit.getSeats() == null ? "" : seatBlit.getSeats() + " /") +
                        String.format(ResourceUtil.getMessage(Response.SEAT_INFORMATION),
                                blitTypeSeat.getSeat().getSectionName(),
                                blitTypeSeat.getSeat().getRowName(),
                                blitTypeSeat.getSeat().getSeatName()));
            });
        }
        log.info("unauthorized user with email '{}' released reserveSeatBlitLock",viewModel.getCustomerEmail());
        salonService.validateNoIndividualSeat(salonService.populateSeatInformationInSalonSchemaByEventDateId(eventDate.getEventDateId()).getSchema());
        seatBlit.setTrackCode(generateTrackCode());
        validateDiscountCodeIfPresentAndCalculateTotalAmount(viewModel,seatBlit);
        return Option.of(paymentRequestService.createPurchaseRequest(seatBlit))
                .map(token -> persistBlit(seatBlit,Optional.empty(),token))
                .map(blit -> paymentRequestService.createZarinpalResponse(blit.getToken()))
                .getOrElseThrow(() -> new RuntimeException("Never Happens"));
    }

    @Transactional
    protected SeatBlit persistBlit(SeatBlit seatBlit, Optional<User> userOptional, String token) {
        userOptional.ifPresent(user -> {
            User attachedUser = userRepository.findOne(user.getUserId());
            seatBlit.setUser(attachedUser);
        });
        seatBlit.setToken(token);
        seatBlit.setPaymentError(ResourceUtil.getMessage(Response.PAYMENT_PENDING));
        seatBlit.setPaymentStatus(PaymentStatus.PENDING.name());
        seatBlit.getBlitTypeSeats().forEach(blitTypeSeat -> blitTypeSeat.setSeatBlit(seatBlit));
        return seatBlitRepository.save(seatBlit);
    }

    @Transactional
    protected SeatBlit reserveFreeBlit(SeatBlit seatBlit, User user) {
        User attachedUser = userRepository.findOne(user.getUserId());
        seatBlit.getBlitTypeSeats()
                .stream().collect(Collectors.groupingBy(BlitTypeSeat::getBlitType,Collectors.counting()))
                .forEach((blitType, aLong) -> {
                    blitType.setSoldCount(blitType.getSoldCount() + aLong.intValue());
                    checkBlitTypeSoldConditionAndSetEventDateEventStateSold(blitType);
                    log.info("****** FREE SEAT BLIT SOLD COUNT RESERVED BY USER '{}' SOLD COUNT IS '{}'", user.getEmail(),
                            blitType.getSoldCount());
                });

        seatBlit.setTrackCode(generateTrackCode());
        seatBlit.setUser(attachedUser);
        seatBlit.setTotalAmount(0L);
        seatBlit.setPrimaryAmount(0L);
        seatBlit.setBankGateway(PaymentStatus.FREE.name());
        seatBlit.setBankGateway(BankGateway.NONE.name());
        seatBlit.getBlitTypeSeats().forEach(blitTypeSeat -> {
            blitTypeSeat.setState(BlitTypeSeatState.SOLD.name());
            blitTypeSeat.setSoldDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()));
            blitTypeSeat.setSeatBlit(seatBlit);
            blitTypeSeat.setReserveDate(null);
        });
        attachedUser.addBlits(seatBlit);
        return seatBlit;
    }

    @Transactional
    public Map<String, Object> generateReservedBlit(ReservedBlitViewModel reservedBlitViewModel, User user) {
        EventDate eventDate  = eventDateRepository.findByEventDateId(reservedBlitViewModel.getEventDateId())
                .orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_DATE_NOT_FOUND)));
        if(eventDate.getEvent().getEventHost().getUser().getUserId()!= user.getUserId()) {
            throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
        }
        BlitTypeSeat blitTypeSeat = blitTypeSeatRepository.findBySeatSeatUidAndBlitTypeEventDateEventDateIdIs(
                reservedBlitViewModel.getSeatUid(),
                reservedBlitViewModel.getEventDateId()).orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.Blit_Type_SEAT_NOT_FOUND)));

        if (!blitTypeSeat.getState().equals(BlitTypeSeatState.NOT_AVAILABLE.name())) {
            throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_RESERVED_FOR_HOST));
        }
        return Option.of(blitTypeSeat.getSeatBlit())
                .map(seatBlit -> excelService.blitMapForPdf(seatBlitMapper.createFromEntity(seatBlit)))
                .getOrElse(() -> {
                    SeatBlit seatBlit = new SeatBlit();
                    seatBlit.setCount(1);
                    seatBlit.setTotalAmount(0L);
                    seatBlit.setUser(user);
                    seatBlit.setTrackCode(generateTrackCode());
                    seatBlit.setEventName(eventDate.getEvent().getEventName());
                    seatBlit.setEventDateAndTime(reservedBlitViewModel.getEventDateAndTime());
                    seatBlit.setCreatedAt(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()));
                    seatBlit.setEventDate(eventDate.getDate());
                    seatBlit.setCustomerName(eventDate.getEvent().getEventHost().getHostName());
                    seatBlit.setCustomerMobileNumber(eventDate.getEvent().getEventHost().getTelephone());
                    seatBlit.setCustomerEmail(user.getEmail());
                    seatBlit.setEventAddress(eventDate.getEvent().getAddress());
                    seatBlit.setBlitTypeName(Constants.HOST_RESERVED_SEATS);
                    seatBlit.setSeatType(SeatType.SEAT_BLIT.name());
                    seatBlit.setPaymentStatus(PaymentStatus.FREE.name());
                    seatBlit.setPrimaryAmount(0L);
                    seatBlit.setBankGateway(BankGateway.NONE.name());
                    seatBlit.setBlitTypeSeats(new HashSet<>(Collections.singletonList(blitTypeSeat)));

                    blitTypeSeat.setState(BlitTypeSeatState.GUEST_NOT_AVAILABLE.name());
                    blitTypeSeat.setSeatBlit(seatBlit);
                    seatBlit.setSeats((seatBlit.getSeats() == null ? "" : seatBlit.getSeats() + " /") +
                            String.format(ResourceUtil.getMessage(Response.SEAT_INFORMATION),
                                    blitTypeSeat.getSeat().getSectionName(),
                                    blitTypeSeat.getSeat().getRowName(),
                                    blitTypeSeat.getSeat().getSeatName()));

                    return excelService.blitMapForPdf(seatBlitMapper.createFromEntity(seatBlitRepository.save(seatBlit)));
                });

    }
}
