package com.blito.services.blit;

import com.blito.configs.Constants;
import com.blito.enums.PaymentStatus;
import com.blito.enums.Response;
import com.blito.enums.SeatType;
import com.blito.enums.State;
import com.blito.exceptions.AdditionalFieldsValidationException;
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
import com.blito.rest.viewmodels.payments.PaymentRequestViewModel;
import com.blito.search.SearchViewModel;
import com.blito.services.*;
import com.blito.services.util.HtmlRenderer;
import io.vavr.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

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
    protected DiscountService discountService;
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

    public abstract Map<String,Object> searchBlitsForExcel(SearchViewModel<E> searchViewModel);
    public abstract Page<V> searchBlits(SearchViewModel<E> searchViewModel, Pageable pageable);
    public abstract Object createBlitAuthorized(V viewModel, User user);
    public abstract PaymentRequestViewModel createUnauthorizedAndNoneFreeBlits(V viewModel);


    BlitType increaseSoldCount(long blitTypeId,E blit) {
        BlitType blitType = blitTypeRepository.findOne(blitTypeId);
        blitType.setSoldCount(blitType.getSoldCount() + blit.getCount());
        checkBlitTypeSoldConditionAndSetEventDateEventStateSold(blitType);
        return blitType;
    }

    public String generateTrackCode() {
        String trackCode = RandomUtil.generateTrackCode();
        if(blitRepository.findByTrackCode(trackCode).isPresent()) {
            return generateTrackCode();
        }
        return trackCode;
    }

    public void checkBlitTypeSoldConditionAndSetEventDateEventStateSold(BlitType blitType) {
        if (blitType.getSoldCount() == blitType.getCapacity()) {
            blitType.setBlitTypeState(State.SOLD.name());
            if (blitType.getEventDate().getBlitTypes().stream().filter(bt -> bt.getName().equals(Constants.HOST_RESERVED_SEATS))
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



    void checkBlitTypeRestrictionsForBuy(BlitType blitType) {

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
