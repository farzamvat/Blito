package com.blito.services.blit;

import com.blito.enums.BankGateway;
import com.blito.enums.PaymentStatus;
import com.blito.enums.Response;
import com.blito.exceptions.NotAllowedException;
import com.blito.exceptions.NotFoundException;
import com.blito.models.BlitType;
import com.blito.models.CommonBlit;
import com.blito.models.User;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.blit.CommonBlitViewModel;
import com.blito.rest.viewmodels.payments.PaymentRequestViewModel;
import com.blito.search.SearchViewModel;
import com.blito.services.ExcelService;
import com.blito.services.SearchService;
import io.vavr.concurrent.Future;
import io.vavr.control.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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

    public void sendEmailAndSmsForPurchasedBlit(CommonBlitViewModel commonBlitViewModel) {
        Map<String, Object> map = new HashMap<>();
        map.put("blit", commonBlitViewModel);
        Future.runRunnable(() -> mailService.sendEmail(commonBlitViewModel.getCustomerEmail(), htmlRenderer.renderHtml("ticket", map),
                ResourceUtil.getMessage(Response.BLIT_RECIEPT)))
                .onFailure((throwable) -> log.debug("exception in sending email '{}'",throwable));
        Future.runRunnable(() -> smsService.sendBlitRecieptSms(commonBlitViewModel.getCustomerMobileNumber(), commonBlitViewModel.getTrackCode()))
                .onFailure(throwable -> log.debug("exception in sending sms '{}'",throwable));
    }

    @Transactional
    @Override
    public Object createBlitAuthorized(CommonBlitViewModel viewModel, User user) {
        CommonBlit blit = commonBlitMapper.createFromViewModel(viewModel);
        BlitType blitType = Optional.ofNullable(blitTypeRepository.findOne(viewModel.getBlitTypeId()))
                .orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_TYPE_NOT_FOUND)));
        checkBlitTypeRestrictionsForBuy(blitType,blit);
        validateAdditionalFields(blitType.getEventDate().getEvent(),blit);
        return blitPurchase(blitType,viewModel,user,blit);
    }

    @Transactional
    @Override
    public CommonBlitViewModel reserveFreeBlitForAuthorizedUser(BlitType blitType, CommonBlit commonBlit, User user) {
        if (commonBlit.getCount() > 10)
            throw new NotAllowedException(ResourceUtil.getMessage(Response.BLIT_COUNT_EXCEEDS_LIMIT));
        // TODO: 10/16/17 bug needs test
        if (commonBlit.getCount() + commonBlitRepository.sumCountBlitByEmailAndBlitTypeId(user.getEmail(),
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
    @Override
    public PaymentRequestViewModel createUnauthorizedAndNoneFreeBlits(CommonBlitViewModel viewModel) {
        CommonBlit commonBlit = commonBlitMapper.createFromViewModel(viewModel);
        BlitType blitType = Optional.ofNullable(blitTypeRepository.findOne(viewModel.getBlitTypeId()))
                .orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_TYPE_NOT_FOUND)));
        if (blitType.isFree())
            throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
        checkBlitTypeRestrictionsForBuy(blitType,commonBlit);
        validateAdditionalFields(blitType.getEventDate().getEvent(),commonBlit);
        validateDiscountCodeIfPresentAndCalculateTotalAmount(viewModel,commonBlit,blitType);
        commonBlit.setTrackCode(generateTrackCode());
        return Option.of(paymentRequestService.createPurchaseRequest(commonBlit))
                .map(token -> persistBlit(blitType,commonBlit,Optional.empty(),token))
                .map(blit -> paymentRequestService.createZarinpalResponse(blit.getToken()))
                .getOrElseThrow(() -> new RuntimeException("Never Happens"));
    }

    @Transactional
    @Override
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
    @Override
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
