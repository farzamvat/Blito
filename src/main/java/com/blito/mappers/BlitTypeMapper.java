package com.blito.mappers;

import org.springframework.stereotype.Component;

import com.blito.models.BlitType;
import com.blito.rest.viewmodels.blittype.BlitTypeCreateViewModel;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;

@Component
public class BlitTypeMapper implements GenericMapper<BlitType,BlitTypeViewModel> {

	public BlitType createFromBlitTypeCreateViewModel(BlitTypeCreateViewModel vmodel)
	{
		BlitType blitType = new BlitType();
		blitType.setCapacity(vmodel.getCapacity());
		blitType.setName(vmodel.getName());
		blitType.setFree(vmodel.isFree());
		blitType.setPrice(vmodel.getPrice());
		return blitType;
	}
	
	@Override
	public BlitType createFromViewModel(BlitTypeViewModel vmodel) {
		BlitType blitType = new BlitType();
		blitType.setName(vmodel.getName());
		blitType.setCapacity(vmodel.getCapacity());
		blitType.setFree(vmodel.isFree());
		blitType.setPrice(vmodel.getPrice());
		return blitType;
	}

	@Override
	public BlitTypeViewModel createFromEntity(BlitType blitType) {
		BlitTypeViewModel vmodel = new BlitTypeViewModel();
		vmodel.setBlitTypeId(blitType.getBlitTypeId());
		vmodel.setName(blitType.getName());
		vmodel.setPrice(blitType.getPrice());
		vmodel.setCapacity(blitType.getCapacity());
		vmodel.setSoldCount(blitType.getSoldCount());
		vmodel.setBlitTypeState(blitType.getBlitTypeState());
		vmodel.setFree(blitType.isFree());
		return vmodel;
	}

	@Override
	public BlitType updateEntity(BlitTypeViewModel viewModel, BlitType entity) {
		return null;
	}

}
