package com.blito.services.blit;

import com.blito.enums.PaymentStatus;
import com.blito.enums.Response;
import com.blito.enums.SeatType;
import com.blito.enums.State;
import com.blito.exceptions.AdditionalFieldsValidationException;
import com.blito.exceptions.InconsistentDataException;
import com.blito.exceptions.NotAllowedException;
import com.blito.exceptions.ResourceNotFoundException;
import com.blito.mappers.CommonBlitMapper;
import com.blito.mappers.SeatBlitMapper;
import com.blito.models.Blit;
import com.blito.models.BlitType;
import com.blito.models.Event;
import com.blito.models.User;
import com.blito.repositories.*;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.blit.AbstractBlitViewModel;
import com.blito.rest.viewmodels.blit.CommonBlitViewModel;
import com.blito.rest.viewmodels.blit.SeatBlitViewModel;
import com.blito.rest.viewmodels.discount.DiscountValidationViewModel;
import com.blito.rest.viewmodels.payments.PaymentRequestViewModel;
import com.blito.services.*;
import com.blito.services.util.HtmlRenderer;
import io.vavr.concurrent.Future;
import io.vavr.control.Option;
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
    SeatBlitRepository seatBlitRepository;

    @Autowired
    public void setSeatBlitRepository(SeatBlitRepository seatBlitRepository) {
        this.seatBlitRepository = seatBlitRepository;
    }

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

    Object blitPurchaseAuthorized(BlitType blitType, V viewModel, User user, E blit) {
        if (blitType.isFree()) {
            return reserveFreeBlitForAuthorizedUser(blitType,blit,user);
        } else {
            validateDiscountCodeIfPresentAndCalculateTotalAmount(viewModel,blit,blitType);
            blit.setTrackCode(generateTrackCode());
            return Option.of(paymentRequestService.createPurchaseRequest(blit))
                    .map(token -> persistBlit(blitType,blit,Optional.of(user),token))
                    .map(b -> paymentRequestService.createZarinpalResponse(b.getToken()))
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

    void validateDiscountCodeIfPresentAndCalculateTotalAmount(V vmodel, E blit, BlitType blitType) {
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
                            CommonBlitViewModel.createResultVmInCaseOfPaymentStatusPending(blit));
                else if (blit.getPaymentStatus().equals(PaymentStatus.ERROR.name()))
                    return new CommonBlitViewModel(
                            CommonBlitViewModel.createResultVmInCaseOfPaymentStatusError(blit));
                else
                    return commonBlitMapper.createFromEntity(commonBlitRepository.findOne(blit.getBlitId()));
            } else {
                if (blit.getPaymentStatus().equals(PaymentStatus.PENDING.name()))
                    return new SeatBlitViewModel(
                            SeatBlitViewModel.createResultVmInCaseOfPaymentStatusPending(blit));
                else if (blit.getPaymentStatus().equals(PaymentStatus.ERROR.name()))
                    return new SeatBlitViewModel(
                            SeatBlitViewModel.createResultVmInCaseOfPaymentStatusError(blit));
                else
                    return seatBlitMapper.createFromEntity(seatBlitRepository.findOne(blit.getBlitId()));
            }
        }).orElseThrow(() -> new ResourceNotFoundException(ResourceUtil.getMessage(Response.BLIT_NOT_FOUND)));
    }

    @Transactional
    protected void validateAdditionalFields(Event event, E blit) {
        if(event.getAdditionalFields() != null && !event.getAdditionalFields().isEmpty()) {
            if(blit.getAdditionalFields().isEmpty())
                throw new AdditionalFieldsValidationException(ResourceUtil.getMessage(Response.ADDITIONAL_FIELDS_CANT_BE_EMPTY));
            else if(blit.getAdditionalFields().size() != event.getAdditionalFields().size())
                throw new AdditionalFieldsValidationException(ResourceUtil.getMessage(Response.ADDITIONAL_FIELDS_VALIDATION_ERROR));
            else if (!blit.getAdditionalFields().keySet().stream().allMatch(key -> event.getAdditionalFields().keySet().contains(key)))
                throw new AdditionalFieldsValidationException(ResourceUtil.getMessage(Response.ADDITIONAL_FIELDS_VALIDATION_ERROR));
        }
    }
    public void sendEmailAndSmsForPurchasedBlit(V viewModel) {
        Map<String, Object> map = new HashMap<>();
        map.put("blit", viewModel);
        Future.runRunnable(() -> mailService.sendEmail(viewModel.getCustomerEmail(),
                htmlRenderer.renderHtml((viewModel instanceof CommonBlitViewModel) ? "ticket" : "ticket_seat", map),
                ResourceUtil.getMessage(Response.BLIT_RECIEPT)))
                .onFailure((throwable) -> log.debug("exception in sending email '{}'",throwable));
        Future.runRunnable(() -> smsService.sendBlitRecieptSms(viewModel.getCustomerMobileNumber(), viewModel.getTrackCode()))
                .onFailure(throwable -> log.debug("exception in sending sms '{}'",throwable));
    }
}
