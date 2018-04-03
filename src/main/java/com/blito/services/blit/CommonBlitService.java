package com.blito.services.blit;

import com.blito.enums.BankGateway;
import com.blito.enums.PaymentStatus;
import com.blito.enums.Response;
import com.blito.enums.SeatType;
import com.blito.exceptions.InconsistentDataException;
import com.blito.exceptions.NotAllowedException;
import com.blito.exceptions.NotFoundException;
import com.blito.exceptions.ValidationException;
import com.blito.models.BlitType;
import com.blito.models.CommonBlit;
import com.blito.models.User;
import com.blito.repositories.BlitRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.blit.CommonBlitViewModel;
import com.blito.rest.viewmodels.discount.DiscountValidationViewModel;
import com.blito.rest.viewmodels.payments.PaymentRequestViewModel;
import com.blito.search.SearchViewModel;
import com.blito.services.ExcelService;
import com.blito.services.SearchService;
import io.vavr.control.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * @author Farzam Vatanzadeh
 * 10/17/17
 * Mailto : farzam.vat@gmail.com
 **/
@Service
public class CommonBlitService extends AbstractBlitService<CommonBlit,CommonBlitViewModel> {
    private final Logger log = LoggerFactory.getLogger(CommonBlitService.class);
    private static final Object reserveFreeCommonBlitLock = new Object();
    @Autowired
    private SearchService searchService;
    @Autowired
    private ExcelService excelService;
    @Autowired
    private BlitRepository blitRepository;

    private Object blitPurchaseAuthorizedCommonBlit(BlitType blitType, CommonBlitViewModel viewModel, User user, CommonBlit blit) {
        if (blitType.isFree()) {
            return reserveFreeBlitForAuthorizedUser(blitType,blit,user);
        } else {
            validateDiscountCodeIfPresentAndCalculateTotalAmount(viewModel,blit,blitType);
            blit.setTrackCode(generateTrackCode());
            return Option.of(paymentRequestService.createPurchaseRequest(blit))
                    .map(token -> persistBlit(blitType,blit,Optional.of(user),token))
                    .map(b -> paymentRequestService.createPaymentRequest(Enum.valueOf(BankGateway.class,b.getBankGateway()),b.getToken()))
                    .getOrElseThrow(() -> new RuntimeException("Never Happens"));
        }
    }

    @Transactional
    @Override
    public Map<String, Object> searchBlitsForExcel(SearchViewModel<CommonBlit> searchViewModel) {
        Set<CommonBlit> blits = searchService.search(searchViewModel, commonBlitRepository);
        if(blits.isEmpty())
            throw new NotFoundException(ResourceUtil.getMessage(Response.SEARCH_UNSUCCESSFUL));
        CommonBlit blit = blits.stream().findAny().get();
        if (blit.getAdditionalFields() != null && !blit.getAdditionalFields().isEmpty()) {
            return excelService.getBlitsExcelMap(commonBlitMapper.createFromEntities(blits), blit.getBlitType().getEventDate().getEvent().getAdditionalFields());
        }
        return excelService.getBlitsExcelMap(commonBlitMapper.createFromEntities(blits));
    }

    @Transactional
    @Override
    public Page<CommonBlitViewModel> searchBlits(SearchViewModel<CommonBlit> searchViewModel, Pageable pageable) {
        return searchService.search(searchViewModel, pageable, commonBlitMapper, commonBlitRepository);
    }

    @Transactional
    @Override
    public Object createBlitAuthorized(CommonBlitViewModel viewModel, User user) {
        if(viewModel.getBlitTypeName() == null || viewModel.getBlitTypeName().isEmpty()) {
            throw new ValidationException(ResourceUtil.getMessage(Response.VALIDATION));
        }
        CommonBlit blit = commonBlitMapper.createFromViewModel(viewModel);
        BlitType blitType = Optional.ofNullable(blitTypeRepository.findOne(viewModel.getBlitTypeId()))
                .orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_TYPE_NOT_FOUND)));
        blit.setEventDateAndTime(blitType.getEventDate().getDateTime());
        checkBlitTypeRestrictionsForBuy(blitType);
        if (blit.getCount() + blitType.getSoldCount() > blitType.getCapacity())
            throw new InconsistentDataException(ResourceUtil.getMessage(Response.REQUESTED_BLIT_COUNT_IS_MORE_THAN_CAPACITY));
        validateAdditionalFields(blitType.getEventDate().getEvent(),blit);
        return blitPurchaseAuthorizedCommonBlit(blitType,viewModel,user,blit);
    }

    @Transactional
    public CommonBlitViewModel reserveFreeBlitForAuthorizedUser(BlitType blitType, CommonBlit commonBlit, User user) {
        if (commonBlit.getCount() > 10)
            throw new NotAllowedException(ResourceUtil.getMessage(Response.BLIT_COUNT_EXCEEDS_LIMIT));
        commonBlitRepository.sumCountBlitByEmailAndBlitTypeId(commonBlit.getCustomerEmail(),
                blitType.getBlitTypeId()).ifPresent(sumCountBlit -> {
                    if(commonBlit.getCount() + sumCountBlit.intValue() > 10 ) {
                        throw new NotAllowedException(ResourceUtil.getMessage(Response.BLIT_COUNT_EXCEEDS_LIMIT_TOTAL));
                    }
        });
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

    private void validateDiscountCodeIfPresentAndCalculateTotalAmount(CommonBlitViewModel vmodel, CommonBlit blit, BlitType blitType) {
        Option.of(vmodel.getDiscountCode())
                .filter(code -> !code.isEmpty())
                .peek(code -> {
                    DiscountValidationViewModel discountValidationViewModel = discountService.validateDiscountCodeBeforePurchaseRequestForCommonBlit(vmodel.getBlitTypeId(),code,vmodel.getCount());
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
            blit.setPrimaryAmount(blit.getTotalAmount());
            if (blit.getCount() * blitType.getPrice() != blit.getTotalAmount())
                throw new InconsistentDataException(ResourceUtil.getMessage(Response.INCONSISTENT_TOTAL_AMOUNT));
        });
    }

    @Transactional
    @Override
    public PaymentRequestViewModel createUnauthorizedAndNoneFreeBlits(CommonBlitViewModel viewModel) {
        if(viewModel.getBlitTypeName() == null || viewModel.getBlitTypeName().isEmpty()) {
            throw new ValidationException(ResourceUtil.getMessage(Response.VALIDATION));
        }
        CommonBlit commonBlit = commonBlitMapper.createFromViewModel(viewModel);
        BlitType blitType = Optional.ofNullable(blitTypeRepository.findOne(viewModel.getBlitTypeId()))
                .orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_TYPE_NOT_FOUND)));
        commonBlit.setEventDateAndTime(blitType.getEventDate().getDateTime());
        if (blitType.isFree())
            throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
        checkBlitTypeRestrictionsForBuy(blitType);
        if (commonBlit.getCount() + blitType.getSoldCount() > blitType.getCapacity())
            throw new InconsistentDataException(ResourceUtil.getMessage(Response.REQUESTED_BLIT_COUNT_IS_MORE_THAN_CAPACITY));
        validateAdditionalFields(blitType.getEventDate().getEvent(),commonBlit);
        validateDiscountCodeIfPresentAndCalculateTotalAmount(viewModel,commonBlit,blitType);
        commonBlit.setTrackCode(generateTrackCode());
        return Option.of(paymentRequestService.createPurchaseRequest(commonBlit))
                .map(token -> persistBlit(blitType,commonBlit,Optional.empty(),token))
                .map(b -> paymentRequestService.createPaymentRequest(Enum.valueOf(BankGateway.class,b.getBankGateway()),b.getToken()))
                .getOrElseThrow(() -> new RuntimeException("Never Happens"));
    }

    @Transactional
    protected CommonBlit persistBlit(BlitType blitType, CommonBlit blit, Optional<User> userOptional, String token) {
        BlitType attachedBlitType = blitTypeRepository.findOne(blitType.getBlitTypeId());
        userOptional.ifPresent(user -> {
            User attachedUser = userRepository.findOne(user.getUserId());
            blit.setUser(attachedUser);
        });
        blit.setBlitType(attachedBlitType);
        blit.setToken(token);
        blit.setPaymentError(ResourceUtil.getMessage(Response.PAYMENT_PENDING));
        blit.setPaymentStatus(PaymentStatus.PENDING.name());
        return commonBlitRepository.save(blit);
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
    public CommonBlit finalizeCommonBlitPayment(CommonBlit commonBlit, Optional<String> refNum) {
        BlitType blitType = commonBlit.getBlitType();
        refNum.ifPresent(rn -> commonBlit.setRefNum(rn));
        commonBlit.setCreatedAt(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()));
        commonBlit.setPaymentStatus(PaymentStatus.PAID.name());
        commonBlit.setPaymentError(ResourceUtil.getMessage(Response.PAYMENT_SUCCESS));
        blitType.setSoldCount(blitType.getSoldCount() + commonBlit.getCount());
        log.info("****** NONE FREE COMMON BLIT SOLD COUNT RESERVED BY USER '{}' SOLD COUNT IS '{}' AND BLIT TYPE CAPACITY IS '{}'",
                commonBlit.getCustomerEmail(),blitType.getSoldCount(),blitType.getCapacity());
        checkBlitTypeSoldConditionAndSetEventDateEventStateSold(blitType);
        discountService.checkIfDiscountCodeExistAndIncrementItsUsage(commonBlit);
        return commonBlit;
    }

    @Transactional
    public Map<String, Object> getBlitPdf(String trackCode) {
        return blitRepository.findByTrackCode(trackCode)
                .map(blit -> {
                    if(blit.getSeatType().equals(SeatType.COMMON.name())) {
                        return commonBlitMapper.createFromEntity(commonBlitRepository.findOne(blit.getBlitId()));
                    } else {
                        return seatBlitMapper.createFromEntity(seatBlitRepository.findOne(blit.getBlitId()));
                    }
                }).map(excelService::blitMapForPdf)
                .orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_NOT_FOUND)));
    }

}
