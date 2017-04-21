package com.blito.mappers;

import org.springframework.stereotype.Component;

import com.blito.models.User;
import com.blito.rest.viewmodels.RegisterVm;
import com.blito.rest.viewmodels.UserInfoViewModel;

@Component
public class UserMapper {
	public User registerViewModeltoUser(RegisterVm vmodel)
	{
		User user = new User();
		user.setFirstname(vmodel.getFirstname());
		user.setLastname(vmodel.getLastname());
		user.setEmail(vmodel.getEmail());
		user.setPassword(vmodel.getPassword());
		user.setMobile(vmodel.getMobile());
		return user;
	}
	
	public UserInfoViewModel userToUserInfoViewModel(User user)
	{
		UserInfoViewModel vmodel = new UserInfoViewModel();
		vmodel.setFirstname(user.getFirstname());
		vmodel.setLastname(user.getLastname());
		vmodel.setMobile(user.getMobile());
		return vmodel;
	}
	
	public User userInfoViewModelToUser(UserInfoViewModel vmodel,User user)
	{
		user.setFirstname(vmodel.getFirstname());
		user.setLastname(vmodel.getLastname());
		user.setMobile(vmodel.getMobile());
		return user;
	}
}
