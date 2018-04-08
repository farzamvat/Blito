package com.blito.mappers;

import com.blito.enums.State;
import com.blito.models.BlitType;
import com.blito.models.EventDate;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class EventDateMapper implements GenericMapper<EventDate,EventDateViewModel> {

	@Autowired
	private BlitTypeMapper blitTypeMapper;
	
	@Override
	public EventDate createFromViewModel(EventDateViewModel vmodel) {
		EventDate eventDate = new EventDate();
		eventDate.setDate(vmodel.getDate());
		eventDate.setDateTime(vmodel.getDateTime());
		eventDate.setUid(vmodel.getUid());
		eventDate.setEventDateState(State.CLOSED.name());
		vmodel.getBlitTypes().forEach(bt -> {
			eventDate.addBlitType(blitTypeMapper.createFromViewModel(bt));
		});
		return eventDate;
	}

	@Override
	public EventDateViewModel createFromEntity(EventDate eventDate) {
		EventDateViewModel vmodel = new EventDateViewModel();
		vmodel.setDate(eventDate.getDate());
		vmodel.setDateTime(eventDate.getDateTime());
		vmodel.setUid(eventDate.getUid());
		vmodel.setBlitTypes(eventDate.getBlitTypes().stream().map(blitTypeMapper::createFromEntity).collect(Collectors.toSet()));
		vmodel.setEventDateId(eventDate.getEventDateId());
		vmodel.setState(Enum.valueOf(State.class, eventDate.getEventDateState()));
		Option.of(eventDate.getSalon()).peek(salon -> vmodel.setHasSalon(true)).onEmpty(() -> vmodel.setHasSalon(false));
		return vmodel;
	}

	@Override
	public EventDate updateEntity(EventDateViewModel vmodel, EventDate eventDate) {
		eventDate.setDate(vmodel.getDate());
		eventDate.setDateTime(vmodel.getDateTime());
		List<String> oldOnes = vmodel.getBlitTypes().stream().map(BlitTypeViewModel::getUid).filter(uid -> !uid.isEmpty()).collect(Collectors.toList());
		List<String> shouldDelete = new ArrayList<>();
		eventDate.getBlitTypes().forEach(bt -> {
			if(!oldOnes.contains(bt.getUid()))
			{
				shouldDelete.add(bt.getUid());
			}
		});
		shouldDelete.forEach(eventDate::removeBlitTypeByUid);
		
		vmodel.getBlitTypes().forEach(bvm ->
			Option.ofOptional(eventDate.getBlitTypes()
					.stream()
					.filter(b -> !bvm.getUid().isEmpty() && b.getUid().equals(bvm.getUid()))
					.findFirst())
					.peek(blitType -> blitTypeMapper.updateEntity(bvm,blitType))
					.onEmpty(() -> {
						BlitType blitType = blitTypeMapper.createFromViewModel(bvm);
						blitType.setUid(UUID.randomUUID().toString());
						eventDate.addBlitType(blitType);
					})
		);
	
		return eventDate;
	}

}
