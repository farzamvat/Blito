package com.blito.services;

import com.blito.enums.OperatorState;
import com.blito.enums.Response;
import com.blito.enums.State;
import com.blito.mappers.DiscountMapper;
import com.blito.models.BlitType;
import com.blito.models.Discount;
import com.blito.models.Event;
import com.blito.models.User;
import com.blito.repositories.BlitTypeRepository;
import com.blito.repositories.DiscountRepository;
import com.blito.repositories.EventRepository;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.discount.DiscountEnableViewModel;
import com.blito.rest.viewmodels.discount.DiscountValidationViewModel;
import com.blito.rest.viewmodels.discount.DiscountViewModel;
import com.blito.rest.viewmodels.exception.ExceptionViewModel;
import com.blito.search.Operation;
import com.blito.search.SearchViewModel;
import com.blito.search.Simple;
import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private DiscountMapper discountMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BlitTypeRepository blitTypeRepository;
    @Autowired
    private SearchService searchService;
    @Autowired
    private EventRepository eventRepository;


    @Transactional
    public Either<ExceptionViewModel, DiscountViewModel> setDiscountCodeByUser(DiscountViewModel vmodel, User user) {
        Set<BlitType> blitTypes = blitTypeRepository.findByBlitTypeIdIn(vmodel.getBlitTypeIds());
        if (!blitTypes.stream().allMatch(blitType -> blitType.getEventDate().getEvent().getEventHost().getUser().getUserId() == user.getUserId())) {
            return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.NOT_ALLOWED), 400));
        }
        return createDiscountCode(vmodel, user, blitTypes);
    }

    @Transactional
    public Either<ExceptionViewModel, DiscountViewModel> setDiscountCodeByOperator(DiscountViewModel vmodel, User user) {
        Set<BlitType> blitTypes = blitTypeRepository.findByBlitTypeIdIn(vmodel.getBlitTypeIds());
        return createDiscountCode(vmodel, user, blitTypes);
    }

    @Transactional
    public Either<ExceptionViewModel, DiscountViewModel> createDiscountCode(DiscountViewModel vmodel, User user, Set<BlitType> blitTypes) {
        Either<ExceptionViewModel, DiscountViewModel> discountViewModelValidation = verifyDiscountViewModel(vmodel, blitTypes);
        if (discountViewModelValidation.isLeft()) {
            return discountViewModelValidation;
        }
        if (discountRepository.findByCode(vmodel.getCode()).isPresent())
            return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.DISCOUNT_CODE_ALREADY_EXISTS), 400));
        Discount discount = discountMapper.createFromViewModel(vmodel);
        discount.setUser(userRepository.findOne(user.getUserId()));
        discount.setBlitTypes(blitTypes);

        discount = discountRepository.save(discount);
        return Either.right(discountMapper.createFromEntity(discount));
    }

    @Transactional
    public Either<ExceptionViewModel, ?> enableDiscountCode(DiscountEnableViewModel viewModel, User user) {
        Optional<Discount> optionalDiscount = Optional.ofNullable(discountRepository.findOne(viewModel.getDiscountId()));
        if (optionalDiscount.isPresent()) {
            Discount discount = optionalDiscount.get();
            if (discount.getUser().getUserId() != user.getUserId()) {
                return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.NOT_ALLOWED), 400));
            } else if(discount.getExpirationDate().before(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()))) {
                return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.DISCOUNT_CODE_EXPIRED),400));
            } else {
                discount.setEnabled(viewModel.getEnable());
                return Either.right(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS), true));
            }
        } else
            return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.DISCOUNT_CODE_NOT_FOUND), 400));
    }

    @Transactional
    public Either<ExceptionViewModel, ?> adminEnableDiscountCode(DiscountEnableViewModel viewModel, User user) {
        Optional<Discount> optionalDiscount = Optional.ofNullable(discountRepository.findOne(viewModel.getDiscountId()));
        if(optionalDiscount.isPresent()) {
            Discount discount = optionalDiscount.get();
            if(discount.getExpirationDate().before(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()))) {
                return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.DISCOUNT_CODE_EXPIRED),400));
            } else {
                discount.setEnabled(viewModel.getEnable());
                return Either.right(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS), true));
            }
        } else {
            return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.DISCOUNT_CODE_NOT_FOUND), 400));
        }
    }

    @Transactional
    public Page<DiscountViewModel> searchDiscountCodes(SearchViewModel<Discount> searchViewModel, Pageable pageable, User user) {
        Simple<Discount> selectDiscountCreator = new Simple<>(Operation.eq,"user-email",user.getEmail());
        searchViewModel.getRestrictions().add(selectDiscountCreator);
        Page<Discount> discounts = searchService.search(searchViewModel, pageable, discountRepository);
        discounts.forEach(discount -> {
            if(discount.getExpirationDate().before(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()))) {
                discount.setEnabled(false);
            }
        });
        return discountMapper.toPage(discounts);
    }
    @Transactional
    public Page<DiscountViewModel> searchDiscountCodesByAdmin(SearchViewModel<Discount> searchViewModel,Pageable pageable) {
        return discountMapper.toPage(searchService.search(searchViewModel,pageable,discountRepository)
        .map(discount -> {
            if(discount.getExpirationDate().before(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()))) {
                discount.setEnabled(false);
            }
            return discount;
        }));
    }

    DiscountValidationViewModel validateDiscountCodeBeforePurchaseRequest(Long blitTypeId, String code, int count) {
        DiscountValidationViewModel discountValidationViewModel = new DiscountValidationViewModel(code, count, blitTypeId);
        return validateDiscountCode(discountValidationViewModel);
    }

    @Transactional
    public DiscountValidationViewModel validateDiscountCode(DiscountValidationViewModel vmodel) {
        return discountRepository.findByCode(vmodel.getCode())
                .map(discount -> {
                    if (!discount.getEnabled()) {
                        vmodel.setValid(false);
                    } else if (discount.getUsed() + vmodel.getCount() > discount.getReusability()) {
                        vmodel.setValid(false);
                    } else if (!discount.getBlitTypes().stream().map(BlitType::getBlitTypeId).collect(Collectors.toList()).contains(vmodel.getBlitTypeId())) {
                        vmodel.setValid(false);
                    } else if (discount.getExpirationDate().before(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()))) {

                        vmodel.setValid(false);
                    } else if (discount.getEffectDate().after(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()))) {
                        vmodel.setValid(false);
                    } else {
                        vmodel.setValid(true);
                        double totalAmount = discount.getBlitTypes()
                                .stream()
                                .filter(blitType -> blitType.getBlitTypeId() == vmodel.getBlitTypeId())
                                .findFirst()
                                .get()
                                .getPrice() * vmodel.getCount() * (100 - discount.getPercentage()) / 100;
                        vmodel.setTotalAmount(Double.valueOf(totalAmount).longValue());
                    }
                    return vmodel;
                })
                .orElseGet(() -> {
                    vmodel.setValid(false);
                    return vmodel;
                });
    }

    @Transactional
    public Either<ExceptionViewModel, DiscountViewModel> updateDiscountCodeByUser(DiscountViewModel vmodel, User user) {
        Set<BlitType> blitTypes = blitTypeRepository.findByBlitTypeIdIn(vmodel.getBlitTypeIds());
        if (!blitTypes.stream().allMatch(blitType -> blitType.getEventDate().getEvent().getEventHost().getUser().getUserId() == user.getUserId())) {
            return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.NOT_ALLOWED), 400));
        }
        return updateDiscountCode(vmodel, user, blitTypes);
    }

    @Transactional
    public Either<ExceptionViewModel, DiscountViewModel> updateDiscountCodeByOperator(DiscountViewModel vmodel, User user) {
        Set<BlitType> blitTypes = blitTypeRepository.findByBlitTypeIdIn(vmodel.getBlitTypeIds());
        return updateDiscountCode(vmodel, user, blitTypes);
    }

    @Transactional
    public Either<ExceptionViewModel, DiscountViewModel> updateDiscountCode(DiscountViewModel vmodel, User user, Set<BlitType> blitTypes) {
        Either<ExceptionViewModel, DiscountViewModel> discountViewModelValidation = verifyDiscountViewModel(vmodel, blitTypes);
        if (discountViewModelValidation.isLeft()) {
            return discountViewModelValidation;
        }
        Optional<Discount> optionalDiscount = discountRepository.findByDiscountId(vmodel.getDiscountId());
        if (!optionalDiscount.isPresent())
            return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.DISCOUNT_CODE_NOT_FOUND), 400));
        Discount discount = optionalDiscount.get();
        if (!discount.getCode().equals(vmodel.getCode()))
            return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.DISCOUNT_CODE_IS_FINAL), 400));
        if (!discount.getPercentage().equals(vmodel.getPercentage()))
            return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.DISCOUNT_PERCENTAGE_IS_FINAL), 400));
        discount = discountMapper.updateEntity(vmodel, discount);
        discount.setUser(userRepository.findOne(user.getUserId()));
        discount.setBlitTypes(blitTypes);

        return Either.right(discountMapper.createFromEntity(discount));
    }

    private Either<ExceptionViewModel, DiscountViewModel> verifyDiscountViewModel(DiscountViewModel vmodel, Set<BlitType> blitTypes) {
        if (blitTypes.isEmpty())
            return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.BLIT_TYPE_NOT_FOUND), 400));
        if (blitTypes.stream().anyMatch(blitType -> blitType.getEventDate().getEvent().getEventState().equals(State.ENDED.name()))) {
            return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.EVENT_NOT_OPEN_DISCOUNT_CODE), 400));
        }
        if (!blitTypes.stream().allMatch(blitType -> blitType.getEventDate().getEvent().getOperatorState().equals(OperatorState.APPROVED.name()))) {
            return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.EVENT_NOT_APPROVED), 400));
        }
        if (vmodel.getEffectDate().after(vmodel.getExpirationDate()))
            return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.INCONSISTENT_DATES), 400));
        if (vmodel.getPercent()) {
            if (!(vmodel.getPercentage() > 0 && vmodel.getPercentage() < 100))
                return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.INCONSISTENT_PERCENT), 400));
            if (vmodel.getAmount() != 0)
                return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.INCONSISTENT_AMOUNT_WHEN_PERCENT_IS_TRUE), 400));
        } else {
            if (vmodel.getAmount() <= 0)
                return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.INCONSISTENT_AMOUNT), 400));
            if (vmodel.getPercentage() > 0)
                return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.INCONSISTENT_PERCENTAGE_WHEN_PERCENT_IS_FALSE), 400));
        }
        return Either.right(vmodel);
    }

    @Transactional
    public Either<ExceptionViewModel, DiscountViewModel> setDiscountForAllEvents(DiscountViewModel vmodel, User user) {
        Set<Event> allEvents = eventRepository.findByOperatorStateIsAndEventStateNotAndIsDeletedFalse(OperatorState.APPROVED.name(), State.ENDED.name());
        Set<BlitType> blitTypes = allEvents.stream().flatMap(e -> e.getEventDates().stream()).flatMap(ed -> ed.getBlitTypes().stream()).collect(Collectors.toSet());
        return createDiscountCode(vmodel, user, blitTypes);
    }
}
