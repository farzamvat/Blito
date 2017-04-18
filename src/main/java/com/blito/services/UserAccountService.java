package com.blito.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blito.enums.Response;
import com.blito.exceptions.EmailAlreadyExistsException;
import com.blito.mappers.RegisterVmToUser;
import com.blito.models.User;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.RegisterVm;

@Service
public class UserAccountService {
	@Autowired UserRepository userRepository;
	@Autowired RegisterVmToUser registerVmToUser;
	@Autowired PasswordEncoder encoder;
	
	public void createUser(RegisterVm vmodel)
	{
		Optional<User> result = userRepository.findByEmail(vmodel.getEmail());
		if(result.isPresent())
		{
			throw new EmailAlreadyExistsException(ResourceUtil.getMessage(Response.EMAIL_ALREADY_IN_USE));
		}
		User user = registerVmToUser.toUser(vmodel);
		user.setPassword(encoder.encode(user.getPassword()));
		userRepository.save(user);
	}
}
