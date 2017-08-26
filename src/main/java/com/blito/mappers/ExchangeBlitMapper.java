package com.blito.mappers;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blito.enums.ExchangeBlitType;
import com.blito.enums.OperatorState;
import com.blito.enums.State;
import com.blito.models.ExchangeBlit;
import com.blito.rest.viewmodels.exchangeblit.ExchangeBlitViewModel;

@Component
public class ExchangeBlitMapper implements GenericMapper<ExchangeBlit, ExchangeBlitViewModel> {
	@Autowired
	ImageMapper imageMapper;

	@Override
	public ExchangeBlit createFromViewModel(ExchangeBlitViewModel vmodel) {
		ExchangeBlit exchangeBlit = new ExchangeBlit();
		exchangeBlit.setTitle(vmodel.getTitle());
		exchangeBlit.setBlitCost(vmodel.getBlitCost());
		exchangeBlit.setDescription(vmodel.getDescription());
		exchangeBlit.setEmail(vmodel.getEmail());
		exchangeBlit.setBlitoEvent(vmodel.isBlitoEvent());
		exchangeBlit.setEventAddress(vmodel.getEventAddress());
		exchangeBlit.setPhoneNumber(vmodel.getPhoneNumber());
		exchangeBlit.setEventDate(vmodel.getEventDate());
		exchangeBlit.setLatitude(vmodel.getLatitude());
		exchangeBlit.setLongitude(vmodel.getLongitude());
		exchangeBlit.setExchangeBlitType(vmodel.getType().name());
		exchangeBlit.setCreatedAt(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()));
		exchangeBlit.setState(State.CLOSED.name());
		exchangeBlit.setOperatorState(OperatorState.PENDING.name());
		exchangeBlit.setDeleted(vmodel.isDeleted());
		return exchangeBlit;
	}

	@Override
	public ExchangeBlitViewModel createFromEntity(ExchangeBlit exchangeBlit) {
		ExchangeBlitViewModel vmodel = new ExchangeBlitViewModel();
		vmodel.setExchangeBlitId(exchangeBlit.getExchangeBlitId());
		vmodel.setTitle(exchangeBlit.getTitle());
		vmodel.setBlitCost(exchangeBlit.getBlitCost());
		vmodel.setDescription(exchangeBlit.getDescription());
		vmodel.setEmail(exchangeBlit.getEmail());
		vmodel.setBlitoEvent(exchangeBlit.isBlitoEvent());
		vmodel.setEventAddress(exchangeBlit.getEventAddress());
		vmodel.setPhoneNumber(exchangeBlit.getPhoneNumber());
		vmodel.setEventDate(exchangeBlit.getEventDate());
		vmodel.setState(Enum.valueOf(State.class, exchangeBlit.getState()));
		vmodel.setOperatorState(Enum.valueOf(OperatorState.class, exchangeBlit.getOperatorState()));
		vmodel.setType(Enum.valueOf(ExchangeBlitType.class, exchangeBlit.getExchangeBlitType()));
		Optional.ofNullable(exchangeBlit.getImage())
		.ifPresent(image -> vmodel.setImage(imageMapper.createFromEntity(image)));
		vmodel.setLatitude(exchangeBlit.getLatitude());
		vmodel.setLongitude(exchangeBlit.getLongitude());
		vmodel.setCreatedAt(exchangeBlit.getCreatedAt());
		vmodel.setExchangeLink(exchangeBlit.getExchangeLink());
		vmodel.setDeleted(exchangeBlit.isDeleted());
		return vmodel;
	}

	@Override
	public ExchangeBlit updateEntity(ExchangeBlitViewModel vmodel, ExchangeBlit exchangeBlit) {
		exchangeBlit.setTitle(vmodel.getTitle());
		exchangeBlit.setBlitCost(vmodel.getBlitCost());
		exchangeBlit.setDescription(vmodel.getDescription());
		exchangeBlit.setEmail(vmodel.getEmail());
		exchangeBlit.setBlitoEvent(vmodel.isBlitoEvent());
		exchangeBlit.setEventAddress(vmodel.getEventAddress());
		exchangeBlit.setPhoneNumber(vmodel.getPhoneNumber());
		exchangeBlit.setEventDate(vmodel.getEventDate());
		exchangeBlit.setLatitude(vmodel.getLatitude());
		exchangeBlit.setLongitude(vmodel.getLongitude());
		exchangeBlit.setOperatorState(OperatorState.PENDING.name());
		exchangeBlit.setState(State.CLOSED.name());
		return exchangeBlit;
	}
}
