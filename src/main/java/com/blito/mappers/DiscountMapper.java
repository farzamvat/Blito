package com.blito.mappers;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.blito.models.Discount;
import com.blito.rest.viewmodels.discount.DiscountViewModel;

@Component
public class DiscountMapper implements GenericMapper<Discount, DiscountViewModel> {

	@Override
	public Discount createFromViewModel(DiscountViewModel vmodel) {
		Discount discount = new Discount();
		discount.setDiscountId(vmodel.getDiscountId());
		discount.setCode(vmodel.getCode());
		discount.setReusability(vmodel.getReusability());
		discount.setEffectDate(vmodel.getEffectDate());
		discount.setUsed(0);
		discount.setExpirationDate(vmodel.getExpirationDate());
		discount.setPercent(vmodel.isPercent());
		discount.setPercent(vmodel.getPercent());
		discount.setAmount(vmodel.getAmount());
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
		vmodel.setPercent(discount.isPercent());
		vmodel.setPercent(discount.getPercent());
		vmodel.setAmount(discount.getAmount());
		vmodel.setUserId(discount.getUser().getUserId());
		vmodel.setBlitTypeIds(
				discount.getBlitTypes().stream().map(bt -> bt.getBlitTypeId()).collect(Collectors.toList()));
		return vmodel;
	}

	@Override
	public Discount updateEntity(DiscountViewModel viewModel, Discount entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
