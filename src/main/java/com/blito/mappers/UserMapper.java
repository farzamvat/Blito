package com.blito.mappers;

import com.blito.models.User;
import com.blito.rest.viewmodels.account.RegisterVm;
import com.blito.rest.viewmodels.account.UserViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper implements GenericMapper <User, UserViewModel> {
	
	@Autowired EventHostMapper eventHostMapper; 
	@Autowired ExchangeBlitMapper exchangeBlitMapper;
	@Autowired CommonBlitMapper BlitMapper;
	
	public User registerViewModeltoUser(RegisterVm vmodel)
	{
		User user = new User();
		user.setFirstname(vmodel.getFirstname());
		user.setLastname(vmodel.getLastname());
		user.setEmail(vmodel.getEmail().toLowerCase());
		user.setPassword(vmodel.getPassword());
		user.setMobile(vmodel.getMobile());
		user.setOldUser(false);
		return user;
	}

	@Override
	public User createFromViewModel(UserViewModel vmodel) {
		User user = new User();
		user.setFirstname(vmodel.getFirstname());
		user.setLastname(vmodel.getLastname());
		user.setMobile(vmodel.getMobile());
		user.setEmail(vmodel.getEmail());
		return null;
	}

	@Override
	public UserViewModel createFromEntity(User user) {
		UserViewModel vmodel = new UserViewModel();
		vmodel.setUserId(user.getUserId());
		vmodel.setFirstname(user.getFirstname());
		vmodel.setLastname(user.getLastname());
		vmodel.setMobile(user.getMobile());
		vmodel.setEmail(user.getEmail());
		vmodel.setActive(user.isActive());
		vmodel.setBanned(user.isBanned());
		vmodel.setEventHosts(eventHostMapper.createFromEntities(user.getEventHosts().stream().filter(eh -> !eh.isDeleted()).collect(Collectors.toSet())));
		vmodel.setExchangeBlits(exchangeBlitMapper.createFromEntities(user.getExchangeBlits().stream().filter(e -> !e.isDeleted()).collect(Collectors.toSet())));
		return vmodel;
	}

	@Override
	public User updateEntity(UserViewModel vmodel, User user) {
		user.setFirstname(vmodel.getFirstname());
		user.setLastname(vmodel.getLastname());
		user.setMobile(vmodel.getMobile());
		return user;
	}
	
	
}
