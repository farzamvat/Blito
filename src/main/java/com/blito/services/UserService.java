package com.blito.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blito.enums.Response;
import com.blito.exceptions.NotFoundException;
import com.blito.models.User;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;

@Service
public class UserService {
	@Autowired UserRepository userRepository;
	
	public User findUserById(long id)
	{
		return Optional.ofNullable(userRepository.findOne(id))
				.map(u -> u)
				.orElseThrow(()-> new NotFoundException(ResourceUtil.getMessage(Response.USER_NOT_FOUND)));
	}
}
