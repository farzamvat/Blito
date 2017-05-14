package com.blito.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blito.models.ExchangeBlit;
import com.blito.rest.viewmodels.exchangeblit.ExchangeBlitViewModel;

@Component
public class ExchangeBlitMapper implements GenericMapper<ExchangeBlit,ExchangeBlitViewModel> {
	@Autowired ImageMapper imageMapper;
	
	@Override
	public ExchangeBlit createFromViewModel(ExchangeBlitViewModel vmodel) {
		ExchangeBlit exchangeBlit = new ExchangeBlit();
		exchangeBlit.setTitle(vmodel.getTitle());
		exchangeBlit.setBlitCost(vmodel.getBlitCost());
		exchangeBlit.setDescription(vmodel.getDescription());
		exchangeBlit.setFirstname(vmodel.getFirstname());
		exchangeBlit.setLastname(vmodel.getLastname());
		exchangeBlit.setEmail(vmodel.getEmail());
		exchangeBlit.setBlitoEvent(vmodel.isBlitoEvent());
		exchangeBlit.setEventAddress(vmodel.getEventAddress());
		exchangeBlit.setPhoneNumber(vmodel.getPhoneNumber());
		exchangeBlit.setEventDate(vmodel.getEventDate());
		exchangeBlit.setVendorAddress(vmodel.getVendorAddress());
		return exchangeBlit;
	}

	@Override
	public ExchangeBlitViewModel createFromEntity(ExchangeBlit exchangeBlit) {
		ExchangeBlitViewModel vmodel = new ExchangeBlitViewModel();
		vmodel.setTitle(exchangeBlit.getTitle());
		vmodel.setBlitCost(exchangeBlit.getBlitCost());
		vmodel.setDescription(exchangeBlit.getDescription());
		vmodel.setFirstname(exchangeBlit.getFirstname());
		vmodel.setLastname(exchangeBlit.getLastname());
		vmodel.setEmail(exchangeBlit.getEmail());
		vmodel.setBlitoEvent(exchangeBlit.isBlitoEvent());
		vmodel.setEventAddress(exchangeBlit.getEventAddress());
		vmodel.setPhoneNumber(exchangeBlit.getPhoneNumber());
		vmodel.setEventDate(exchangeBlit.getEventDate());
		vmodel.setVendorAddress(exchangeBlit.getVendorAddress());
		vmodel.setState(exchangeBlit.getState());
		vmodel.setOperatorState(exchangeBlit.getOperatorState());
		vmodel.setType(exchangeBlit.getType());
		vmodel.setImage(imageMapper.createFromEntity(exchangeBlit.getImage()));
		return vmodel;
	}

	@Override
	public ExchangeBlit updateEntity(ExchangeBlitViewModel vmodel, ExchangeBlit exchangeBlit) {
		exchangeBlit.setTitle(vmodel.getTitle());
		exchangeBlit.setBlitCost(vmodel.getBlitCost());
		exchangeBlit.setDescription(vmodel.getDescription());
		exchangeBlit.setFirstname(vmodel.getFirstname());
		exchangeBlit.setLastname(vmodel.getLastname());
		exchangeBlit.setEmail(vmodel.getEmail());
		exchangeBlit.setBlitoEvent(vmodel.isBlitoEvent());
		exchangeBlit.setEventAddress(vmodel.getEventAddress());
		exchangeBlit.setPhoneNumber(vmodel.getPhoneNumber());
		exchangeBlit.setEventDate(vmodel.getEventDate());
		exchangeBlit.setVendorAddress(vmodel.getVendorAddress());
		return exchangeBlit;
	}
}
