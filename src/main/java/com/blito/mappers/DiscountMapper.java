package com.blito.mappers;

import com.blito.models.Discount;
import com.blito.rest.viewmodels.discount.DiscountViewModel;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DiscountMapper implements GenericMapper<Discount, DiscountViewModel> {

	@Override
	public Discount createFromViewModel(DiscountViewModel vmodel) {
		Discount discount = new Discount();
		discount.setCode(vmodel.getCode());
		discount.setReusability(vmodel.getReusability());
		discount.setEffectDate(vmodel.getEffectDate());
		discount.setUsed(0);
		discount.setExpirationDate(vmodel.getExpirationDate());
		discount.setPercentage(vmodel.getPercentage());
		discount.setPercent(vmodel.getPercent());
		discount.setAmount(vmodel.getAmount());
		discount.setEnabled(true);
		return discount;
	}

	@Override
	public DiscountViewModel createFromEntity(Discount discount) {
		DiscountViewModel vmodel = new DiscountViewModel();
		vmodel.setDiscountId(discount.getDiscountId());
		vmodel.setCode(discount.getCode());
		vmodel.setReusability(discount.getReusability());
		vmodel.setUsed(discount.getUsed());
		vmodel.setEffectDate(discount.getEffectDate());
		vmodel.setExpirationDate(discount.getExpirationDate());
		vmodel.setPercentage(discount.getPercentage());
		vmodel.setEnabled(discount.getEnabled());
		vmodel.setPercent(discount.getPercent());
		vmodel.setAmount(discount.getAmount());
		vmodel.setBlitTypeIds(
				discount.getBlitTypes().stream().map(bt -> bt.getBlitTypeId()).collect(Collectors.toSet()));
		return vmodel;
	}

	@Override
	public Discount updateEntity(DiscountViewModel vmodel, Discount discount) {
//        discount.setCode(vmodel.getCode());
        discount.setReusability(vmodel.getReusability());
        discount.setEffectDate(vmodel.getEffectDate());
        discount.setUsed(vmodel.getUsed());
        discount.setExpirationDate(vmodel.getExpirationDate());
//        discount.setPercentage(vmodel.getPercentage());
        discount.setPercent(vmodel.getPercent());
        discount.setAmount(vmodel.getAmount());
        return discount;
	}

}
