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
import java.util.concurrent.CompletableFuture;

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

    @Transactional
    public DiscountValidationViewModel validateDiscountCode(DiscountValidationViewModel vmodel){
        if(!discountRepository.findByCode(vmodel.getCode()).isPresent()){
            vmodel.setValid(false);
            return vmodel;
        }
        Discount discount = discountRepository.findByCode(vmodel.getCode()).get();
        if(discount.getUsed()+vmodel.getCount() > discount.getReusability()){
            vmodel.setValid(false);
        }
        else if(discount.getBlitTypes().stream().map(bt -> bt.getBlitTypeId()).noneMatch(blitTypeId->blitTypeId.equals(vmodel.getBlitTypeId()))){
            vmodel.setValid(false);
        }
        else if(discount.getExpirationDate().before(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()))){
            vmodel.setValid(false);
        }
        else if(discount.getEffectDate().after(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()))){
            vmodel.setValid(false);
        }
        else {
            vmodel.setValid(true);
            double totalAmount = blitTypeRepository.findByBlitTypeId(vmodel.getBlitTypeId()).getPrice()*vmodel.getCount()*(100-discount.getPercent())/100;
            vmodel.setTotalAmount(totalAmount);
        }
        return vmodel;
    }


}
