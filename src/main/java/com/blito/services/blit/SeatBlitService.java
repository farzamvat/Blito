package com.blito.services.blit;

import com.blito.enums.BankGateway;
import com.blito.enums.BlitTypeSeatState;
import com.blito.enums.PaymentStatus;
import com.blito.enums.Response;
import com.blito.exceptions.NotAllowedException;
import com.blito.exceptions.NotFoundException;
import com.blito.exceptions.SeatException;
import com.blito.models.BlitType;
import com.blito.models.BlitTypeSeat;
import com.blito.models.SeatBlit;
import com.blito.models.User;
import com.blito.repositories.BlitTypeSeatRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.blit.SeatBlitViewModel;
import com.blito.rest.viewmodels.blit.SeatErrorViewModel;
import com.blito.rest.viewmodels.payments.PaymentRequestViewModel;
import com.blito.services.SalonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Farzam Vatanzadeh
 * 10/17/17
 * Mailto : farzam.vat@gmail.com
 **/

@Service
public class SeatBlitService extends AbstractBlitService<SeatBlit,SeatBlitViewModel> {
    private final Logger log = LoggerFactory.getLogger(SeatBlitService.class);
    private static final Object reserveFreeSeatBlitLock = new Object();

    private BlitTypeSeatRepository blitTypeSeatRepository;
    private ObjectMapper objectMapper;
    private SalonService salonService;

    @Autowired
    public void setSalonService(SalonService salonService) {
        this.salonService = salonService;
    }

    @Autowired
    public void setBlitTypeSeatRepository(BlitTypeSeatRepository blitTypeSeatRepository) {
        this.blitTypeSeatRepository = blitTypeSeatRepository;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
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
    public Object createBlitAuthorized(SeatBlitViewModel viewModel, User user) {
        SeatBlit seatBlit = seatBlitMapper.createFromViewModel(viewModel);
        BlitType blitType = Optional.ofNullable(blitTypeRepository.findOne(viewModel.getBlitTypeId()))
                .orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_TYPE_NOT_FOUND)));
        checkBlitTypeRestrictionsForBuy(blitType,seatBlit);
        Set<BlitTypeSeat> blitTypeSeats =
                blitTypeSeatRepository.findBySeatSeatUidInAndBlitTypeEventDateEventDateId(viewModel.getSeatUids(),viewModel.getEventDateId());
        validateAdditionalFields(blitType.getEventDate().getEvent(),seatBlit);
        validateSeatBlitForBuy(blitTypeSeats);
        blitTypeSeats.forEach(blitTypeSeat -> {
            blitTypeSeat.setState(BlitTypeSeatState.RESERVED.name());
            blitTypeSeat.setReserveDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()));
        });
        salonService.validateNoIndividualSeat(salonService.populateSeatInformationsInSalonSchemaByEventDateId(blitType.getEventDate().getEventDateId()));
        seatBlit.setBlitTypeSeats(blitTypeSeats);
        return blitPurchaseAuthorized(blitType,viewModel,user,seatBlit);
    }



    @Transactional
    @Override
    public SeatBlitViewModel reserveFreeBlitForAuthorizedUser(BlitType blitType, SeatBlit seatBlit, User user) {
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
                    .createFromEntity(reserveFreeBlit(blitType,seatBlit,user));
            log.info("User with email '{}' released the lock",user.getEmail());
        }
        // UNLOCK
		sendEmailAndSmsForPurchasedBlit(responseBlit);
        return responseBlit;
    }

    @Transactional
    @Override
    public PaymentRequestViewModel createUnauthorizedAndNoneFreeBlits(SeatBlitViewModel viewModel) {
        SeatBlit seatBlit = seatBlitMapper.createFromViewModel(viewModel);
        BlitType blitType = Optional.ofNullable(blitTypeRepository.findOne(viewModel.getBlitTypeId()))
                .orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_TYPE_NOT_FOUND)));
        if (blitType.isFree())
            throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
        checkBlitTypeRestrictionsForBuy(blitType,seatBlit);
        validateAdditionalFields(blitType.getEventDate().getEvent(),seatBlit);
        validateDiscountCodeIfPresentAndCalculateTotalAmount(viewModel,seatBlit,blitType);
        Set<BlitTypeSeat> blitTypeSeats =
                blitTypeSeatRepository.findBySeatSeatUidInAndBlitTypeEventDateEventDateId(viewModel.getSeatUids(),viewModel.getEventDateId());
        validateSeatBlitForBuy(blitTypeSeats);
        blitTypeSeats.forEach(blitTypeSeat -> {
            blitTypeSeat.setState(BlitTypeSeatState.RESERVED.name());
            blitTypeSeat.setReserveDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()));
        });
        salonService.validateNoIndividualSeat(salonService.populateSeatInformationsInSalonSchemaByEventDateId(blitType.getEventDate().getEventDateId()));
        seatBlit.setTrackCode(generateTrackCode());
        return Option.of(paymentRequestService.createPurchaseRequest(seatBlit))
                .map(token -> persistBlit(blitType,seatBlit,Optional.empty(),token))
                .map(blit -> paymentRequestService.createZarinpalResponse(blit.getToken()))
                .getOrElseThrow(() -> new RuntimeException("Never Happens"));
    }

    @Transactional
    @Override
    protected SeatBlit persistBlit(BlitType blitType, SeatBlit seatBlit, Optional<User> userOptional, String token) {
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
    @Override
    protected SeatBlit reserveFreeBlit(BlitType blitType, SeatBlit seatBlit, User user) {
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
            blitTypeSeat.setSeatBlit(seatBlit);
            blitTypeSeat.setReserveDate(null);
        });
        attachedUser.addBlits(seatBlit);
        return seatBlit;
    }
}
