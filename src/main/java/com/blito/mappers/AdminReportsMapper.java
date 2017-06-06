package com.blito.mappers;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.blito.models.CommonBlit;
import com.blito.rest.viewmodels.adminreport.BlitBuyerViewModel;

@Component
public class AdminReportsMapper {
	
	public BlitBuyerViewModel toBlitBuyerReport(CommonBlit blit) {
		BlitBuyerViewModel vmodel = new BlitBuyerViewModel();
		vmodel.setUserId(blit.getUser().getUserId());
		vmodel.setBlitId(blit.getBlitId());
		vmodel.setFirstname(blit.getUser().getFirstname());
		vmodel.setLastname(blit.getUser().getLastname());
		vmodel.setTrackCode(blit.getTrackCode());
		vmodel.setMobile(blit.getUser().getMobile());
		vmodel.setBlitTypeName(blit.getBlitType().getName());
		return vmodel;
	}

	
	public <T,U> List<U> toList(List<T> list,Function<T,U> function)
	{
		return list.stream().map(function::apply).collect(Collectors.toList());
	}
	
	public <T,U> Page<U> toPage(Page<T> page,Function<T,U> function)
	{
		return page.map(function::apply);
	}
}
