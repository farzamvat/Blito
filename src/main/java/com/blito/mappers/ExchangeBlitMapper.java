package com.blito.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.blito.models.ExchangeBlit;
import com.blito.rest.viewmodels.exchangeblit.AdminExchangeBlitViewModel;
import com.blito.rest.viewmodels.exchangeblit.ApprovedExchangeBlitViewModel;
import com.blito.rest.viewmodels.exchangeblit.ExchangeBlitViewModel;
import com.blito.rest.viewmodels.exchangeblit.SimpleExchangeBlitViewModel;
import com.blito.rest.viewmodels.exchangeblit.UserEditExchangeBlitViewModel;

@Component
public class ExchangeBlitMapper implements GenericMapper<ExchangeBlit,ExchangeBlitViewModel> {
	
	public SimpleExchangeBlitViewModel exchangeBlitToSimpleViewModel(ExchangeBlit exchangeBlit)
	{
		SimpleExchangeBlitViewModel vmodel = new SimpleExchangeBlitViewModel();
		vmodel.setTitle(exchangeBlit.getTitle());
		vmodel.setExchangeBlitId(exchangeBlit.getExchangeBlitId());
		vmodel.setBlitCost(exchangeBlit.getBlitCost());
		vmodel.setEventDate(exchangeBlit.getEventDate());
		vmodel.setState(exchangeBlit.getState());
		vmodel.setOperatorState(exchangeBlit.getOperatorState());
		vmodel.setType(exchangeBlit.getType());
		return vmodel;
	}
	
	public ApprovedExchangeBlitViewModel exchangeBlitToApprovedViewModel(ExchangeBlit exchangeBlit)
	{
		ApprovedExchangeBlitViewModel vmodel = new ApprovedExchangeBlitViewModel();
		vmodel.setEventDate(exchangeBlit.getEventDate());
		vmodel.setBlitCost(exchangeBlit.getBlitCost());
		vmodel.setDescription(exchangeBlit.getDescription());
		vmodel.setType(exchangeBlit.getType());
		vmodel.setTitle(exchangeBlit.getTitle());
		return vmodel;
	}
	
	public UserEditExchangeBlitViewModel exchangeBlitToUserEditViewModel (ExchangeBlit exchangeBlit)
	{
		UserEditExchangeBlitViewModel vmodel = new UserEditExchangeBlitViewModel();
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
		vmodel.setType(exchangeBlit.getType());
		return vmodel;
	}
	
	public ExchangeBlit userEditViewModelToExchangeBlit(UserEditExchangeBlitViewModel vmodel,ExchangeBlit exchangeBlit)
	{
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
		exchangeBlit.setState(vmodel.getState());
		exchangeBlit.setType(vmodel.getType());
		return exchangeBlit;
	}
	
	public AdminExchangeBlitViewModel exchangeBlitToAdminViewModel(ExchangeBlit exchangeBlit)
	{
		AdminExchangeBlitViewModel vmodel = new AdminExchangeBlitViewModel();
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
		vmodel.setUserId(exchangeBlit.getUser().getUserId());
		return vmodel;
	}
	
	
	
	public List<AdminExchangeBlitViewModel> exchangeBlitsToAdminViewModels(List<ExchangeBlit> exchangeBlits)
	{
		return toList(exchangeBlits, this::exchangeBlitToAdminViewModel);
	}
	
	public List<SimpleExchangeBlitViewModel> exchangeBlitsToSimpleViewModels(List<ExchangeBlit> exchangeBlits)
	{
		return toList(exchangeBlits, this::exchangeBlitToSimpleViewModel);
	}


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
		return vmodel;
	}

	@Override
	public ExchangeBlit updateEntity(ExchangeBlitViewModel viewModel, ExchangeBlit entity) {
		// TODO Auto-generated method stub
		return null;
	}
}
