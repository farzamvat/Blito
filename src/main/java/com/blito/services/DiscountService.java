package com.blito.services;

import com.blito.enums.Response;
import com.blito.mappers.DiscountMapper;
import com.blito.mappers.EventMapper;
import com.blito.models.Discount;
import com.blito.models.Event;
import com.blito.models.User;
import com.blito.repositories.BlitTypeRepository;
import com.blito.repositories.DiscountRepository;
import com.blito.repositories.EventRepository;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.discount.DiscountValidationViewModel;
import com.blito.rest.viewmodels.discount.DiscountViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.exception.ExceptionViewModel;
import com.blito.security.SecurityContextHolder;
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


    @Transactional
    public Either<ExceptionViewModel,DiscountViewModel> setDiscountCode(DiscountViewModel vmodel, User user) {

        if (vmodel.getEffectDate().after(vmodel.getExpirationDate()))
            return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.INCONSISTENT_DATES),400));
        if (vmodel.isPercent()) {
            if (!(vmodel.getPercentage() > 0 && vmodel.getPercentage() < 100))
                return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.INCONSISTENT_PERCENT),400));
            if (vmodel.getAmount() != 0)
                return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.INCONSISTENT_AMOUNT_WHEN_PERCENT_IS_TRUE),400));
        } else {
            if (vmodel.getAmount() <= 0)
                return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.INCONSISTENT_AMOUNT),400));
            if (vmodel.getPercentage() > 0)
                return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.INCONSISTENT_PERCENTAGE_WHEN_PERCENT_IS_FALSE),400));
        }
        if (discountRepository.findByCode(vmodel.getCode()).isPresent())
            return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.DISCOUNT_CODE_ALREADY_EXISTS),400));
        Discount discount = discountMapper.createFromViewModel(vmodel);
        discount.setUser(userRepository.findOne(user.getUserId()));
        discount.setBlitTypes(blitTypeRepository.findByBlitTypeIdIn(vmodel.getBlitTypeIds()));
        if(discount.getBlitTypes().isEmpty())
            return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.BLIT_TYPE_NOT_FOUND),400));
        discount = discountRepository.save(discount);
        return Either.right(discountMapper.createFromEntity(discount));
    }




}
