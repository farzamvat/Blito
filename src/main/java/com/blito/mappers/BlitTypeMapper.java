package com.blito.mappers;

import org.springframework.stereotype.Component;

import com.blito.enums.State;
import com.blito.models.BlitType;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;

@Component
public class BlitTypeMapper implements GenericMapper<BlitType,BlitTypeViewModel> {
	
	@Override
	public BlitType createFromViewModel(BlitTypeViewModel vmodel) {
		BlitType blitType = new BlitType();
		blitType.setName(vmodel.getName());
		blitType.setCapacity(vmodel.getCapacity());
		blitType.setFree(vmodel.isFree());
		blitType.setPrice(vmodel.getPrice());
		blitType.setBlitTypeState(State.CLOSED.name());
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
		vmodel.setBlitTypeState(Enum.valueOf(State.class, blitType.getBlitTypeState()));
		vmodel.setFree(blitType.isFree());
		vmodel.setSoldCount(blitType.getSoldCount());
		return vmodel;
	}

	@Override
	public BlitType updateEntity(BlitTypeViewModel vmodel, BlitType blitType) {
		
		blitType.setName(vmodel.getName());
		blitType.setCapacity(vmodel.getCapacity());
		blitType.setFree(vmodel.isFree());
		blitType.setPrice(vmodel.getPrice());
		blitType.setBlitTypeState(State.CLOSED.name());
		return blitType;
	}

}
