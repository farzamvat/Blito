package com.blito.services.blit;

import com.blito.enums.*;
import com.blito.exceptions.*;
import com.blito.mappers.CommonBlitMapper;
import com.blito.mappers.SeatBlitMapper;
import com.blito.models.*;
import com.blito.repositories.BlitRepository;
import com.blito.repositories.BlitTypeRepository;
import com.blito.repositories.CommonBlitRepository;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.blit.AbstractBlitViewModel;
import com.blito.rest.viewmodels.blit.CommonBlitViewModel;
import com.blito.rest.viewmodels.blit.SeatErrorViewModel;
import com.blito.rest.viewmodels.discount.DiscountValidationViewModel;
import com.blito.rest.viewmodels.payments.PaymentRequestViewModel;
import com.blito.services.*;
import com.blito.services.util.HtmlRenderer;
import io.vavr.concurrent.Future;
import io.vavr.control.Option;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Farzam Vatanzadeh
 * 10/17/17
 * Mailto : farzam.vat@gmail.com
 **/

public abstract class AbstractBlitService <E extends Blit,V extends AbstractBlitViewModel> {
    private final Logger log = LoggerFactory.getLogger(AbstractBlitService.class);

    protected UserRepository userRepository;
    protected BlitRepository blitRepository;
    CommonBlitMapper commonBlitMapper;
    SeatBlitMapper seatBlitMapper;
    protected MailService mailService;
    protected SmsService smsService;
    protected HtmlRenderer htmlRenderer;
    private DiscountService discountService;
    protected BlitTypeRepository blitTypeRepository;
    PaymentRequestService paymentRequestService;
    CommonBlitRepository commonBlitRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    public void setBlitRepository(BlitRepository blitRepository) {
        this.blitRepository = blitRepository;
    }
    @Autowired
    public void setCommonBlitMapper(CommonBlitMapper commonBlitMapper) {
        this.commonBlitMapper = commonBlitMapper;
    }
    @Autowired
    public void setSeatBlitMapper(SeatBlitMapper seatBlitMapper) {
        this.seatBlitMapper = seatBlitMapper;
    }
    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }
    @Autowired
    public void setSmsService(SmsService smsService) {
        this.smsService = smsService;
    }
    @Autowired
    public void setHtmlRenderer(HtmlRenderer htmlRenderer) {
        this.htmlRenderer = htmlRenderer;
    }
    @Autowired
    public void setDiscountService(DiscountService discountService) {
        this.discountService = discountService;
    }
    @Autowired
    public void setBlitTypeRepository(BlitTypeRepository blitTypeRepository) {
        this.blitTypeRepository = blitTypeRepository;
    }
    @Autowired
    public void setPaymentRequestService(PaymentRequestService paymentRequestService) {
        this.paymentRequestService = paymentRequestService;
    }
    @Autowired
    public void setCommonBlitRepository(CommonBlitRepository commonBlitRepository) {
        this.commonBlitRepository = commonBlitRepository;
    }

    public abstract Object createBlitAuthorized(V viewModel, User user);
    public abstract V reserveFreeBlitForAuthorizedUser(BlitType blitType, E blit, User user);
    public abstract PaymentRequestViewModel createUnauthorizedAndNoneFreeBlits(V viewModel);
    protected abstract E persistBlit(BlitType blitType,E blit,Optional<User> userOptional,String token);
    protected abstract E reserveFreeBlit(BlitType blitType, E blit, User user);

    Object blitPurchase(BlitType blitType, V viewModel, User user, E blit) {
        if (blitType.isFree()) {
            return reserveFreeBlitForAuthorizedUser(blitType,blit,user);
        } else {
            validateDiscountCodeIfPresentAndCalculateTotalAmount(viewModel,blit,blitType);
            blit.setTrackCode(generateTrackCode());
            return Option.of(paymentRequestService.createPurchaseRequest(blit))
                    .map(token -> persistBlit(blitType,blit,Optional.of(user),token))
                    .map(commonBlit -> paymentRequestService.createZarinpalResponse(commonBlit.getToken()))
                    .getOrElseThrow(() -> new RuntimeException("Never Happens"));
        }
    }

    BlitType increaseSoldCount(long blitTypeId,E blit) {
        BlitType blitType = blitTypeRepository.findOne(blitTypeId);
        blitType.setSoldCount(blitType.getSoldCount() + blit.getCount());
        checkBlitTypeSoldConditionAndSetEventDateEventStateSold(blitType);
        return blitType;
    }

    String generateTrackCode() {
        String trackCode = RandomUtil.generateTrackCode();
        while (blitRepository.findByTrackCode(trackCode).isPresent()) {
            return generateTrackCode();
        }
        return trackCode;
    }

    private void checkBlitTypeSoldConditionAndSetEventDateEventStateSold(BlitType blitType) {
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
    void checkBlitTypeRestrictionsForBuy(BlitType blitType, E blit) {

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

    void validateDiscountCodeIfPresentAndCalculateTotalAmount(V vmodel, Blit blit, BlitType blitType) {
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

    void validateSeatBlitForBuy(Set<BlitTypeSeat> blitTypeSeats) {

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
    protected void validateAdditionalFields(Event event, Blit blit) {
        if(event.getAdditionalFields() != null && !event.getAdditionalFields().isEmpty()) {
            if(blit.getAdditionalFields().isEmpty())
                throw new AdditionalFieldsValidationException(ResourceUtil.getMessage(Response.ADDITIONAL_FIELDS_CANT_BE_EMPTY));
            else if(blit.getAdditionalFields().size() != event.getAdditionalFields().size())
                throw new AdditionalFieldsValidationException(ResourceUtil.getMessage(Response.ADDITIONAL_FIELDS_VALIDATION_ERROR));
            else if (!blit.getAdditionalFields().keySet().stream().allMatch(key -> event.getAdditionalFields().keySet().contains(key)))
                throw new AdditionalFieldsValidationException(ResourceUtil.getMessage(Response.ADDITIONAL_FIELDS_VALIDATION_ERROR));
        }
    }
    protected void sendEmailAndSmsForPurchasedBlit(CommonBlitViewModel commonBlitViewModel) {
        Map<String, Object> map = new HashMap<>();
        map.put("blit", commonBlitViewModel);
        Future.runRunnable(() -> mailService.sendEmail(commonBlitViewModel.getCustomerEmail(), htmlRenderer.renderHtml("ticket", map),
                ResourceUtil.getMessage(Response.BLIT_RECIEPT)))
                .onFailure((throwable) -> log.debug("exception in sending email '{}'",throwable));
        Future.runRunnable(() -> smsService.sendBlitRecieptSms(commonBlitViewModel.getCustomerMobileNumber(), commonBlitViewModel.getTrackCode()))
                .onFailure(throwable -> log.debug("exception in sending sms '{}'",throwable));
    }
}
