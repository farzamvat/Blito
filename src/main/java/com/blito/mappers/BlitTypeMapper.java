package com.blito.mappers;

import com.blito.enums.BlitTypeSeatState;
import com.blito.enums.State;
import com.blito.models.BlitType;
import com.blito.repositories.BlitTypeSeatRepository;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;
import com.blito.services.SeatPickerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

@Component
public class BlitTypeMapper implements GenericMapper<BlitType,BlitTypeViewModel> {

	private SeatPickerService seatPickerService;
	private BlitTypeSeatRepository blitTypeSeatRepository;

	@Autowired
	public void setSeatPickerService(SeatPickerService seatPickerService) {
		this.seatPickerService = seatPickerService;
	}
	@Autowired
	public void setBlitTypeSeatRepository(BlitTypeSeatRepository blitTypeSeatRepository) {
		this.blitTypeSeatRepository = blitTypeSeatRepository;
	}

	@Override
	public BlitType createFromViewModel(BlitTypeViewModel vmodel) {
		BlitType blitType = new BlitType();
		blitType.setName(vmodel.getName());
		blitType.setCapacity(vmodel.getCapacity());
		blitType.setFree(vmodel.isFree());
		blitType.setPrice(vmodel.getPrice());
		blitType.setBlitTypeState(State.CLOSED.name());
		Optional.ofNullable(vmodel.getSeatUids()).filter(seatUids -> !seatUids.isEmpty())
				.ifPresent(seatUids -> blitType.setBlitTypeSeats(seatPickerService.createBlitTypeSeats(vmodel,blitType)));
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
		Optional<Set<String>> seatUids = Optional.ofNullable(vmodel.getSeatUids()).filter(uids -> !uids.isEmpty());
		if(seatUids.isPresent()) {
			blitType.setBlitTypeSeats(seatPickerService.updateBlitTypeSeats(vmodel,blitType));
		} else {
			blitTypeSeatRepository.deleteByBlitTypeBlitTypeIdAndStateNotIn(blitType.getBlitTypeId(),Arrays.asList(BlitTypeSeatState.SOLD.name(),BlitTypeSeatState.RESERVED.name()));
		}
		return blitType;
	}
}