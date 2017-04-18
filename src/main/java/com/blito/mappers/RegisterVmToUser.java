package com.blito.mappers;

import org.springframework.stereotype.Component;

import com.blito.models.User;
import com.blito.rest.viewmodels.RegisterVm;

@Component
public class RegisterVmToUser {
	public User toUser(RegisterVm vmodel)
	{
		User user = new User();
		user.setFirstname(vmodel.getFirstname());
		user.setLastname(vmodel.getLastname());
		user.setEmail(vmodel.getEmail());
		user.setPassword(vmodel.getPassword());
		user.setMobile(vmodel.getMobile());
		return user;
	}
}
