package com.blito.mappers;

import java.util.List;
import java.util.stream.Collectors;

import com.blito.models.ExchangeBlit;
import com.blito.rest.viewmodels.ExchangeBlitViewModel;

public class ExchangeBlitMapper {
	public ExchangeBlit ViewModelToExchangeBlit(ExchangeBlitViewModel vmodel,ExchangeBlit exchangeBlit)
	{
		exchangeBlit.setBlitCost(vmodel.getBlitCost());
		exchangeBlit.setDescription(vmodel.getDescription());
		exchangeBlit.setFisrtname(vmodel.getFisrtname());
		exchangeBlit.setLastname(vmodel.getLastname());
		exchangeBlit.setEmail(vmodel.getEmail());
		exchangeBlit.setBlitoEvent(vmodel.isBlitoEvent());
		exchangeBlit.setEventAddress(vmodel.getEventAddress());
		exchangeBlit.setPhoneNumber(vmodel.getPhoneNumber());
		exchangeBlit.setEventDate(vmodel.getEventDate());
		exchangeBlit.setVendorAddress(vmodel.getVendorAddress());
		return exchangeBlit;
	}
	
	public ExchangeBlitViewModel exchangeBlitToViewModel(ExchangeBlit exchangeBlit)
	{
		ExchangeBlitViewModel vmodel = new ExchangeBlitViewModel();
		vmodel.setBlitCost(exchangeBlit.getBlitCost());
		vmodel.setDescription(exchangeBlit.getDescription());
		vmodel.setFisrtname(exchangeBlit.getFisrtname());
		vmodel.setLastname(exchangeBlit.getLastname());
		vmodel.setEmail(exchangeBlit.getEmail());
		vmodel.setBlitoEvent(exchangeBlit.isBlitoEvent());
		vmodel.setEventAddress(exchangeBlit.getEventAddress());
		vmodel.setPhoneNumber(exchangeBlit.getPhoneNumber());
		vmodel.setEventDate(exchangeBlit.getEventDate());
		vmodel.setVendorAddress(exchangeBlit.getVendorAddress());
		vmodel.setState(exchangeBlit.getState());
		vmodel.setOperatorState(exchangeBlit.getOperatorState());
		return vmodel;
	}
	
	public List<ExchangeBlitViewModel> exchangeBlitsToViewModels(List<ExchangeBlit> exchangeBlits)
	{
		return exchangeBlits.stream().map(e -> exchangeBlitToViewModel(e)).collect(Collectors.toList());
	}
}
