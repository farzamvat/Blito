package com.blito.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.blito.enums.Response;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.UserMapper;
import com.blito.models.User;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.account.UserAdminUpdateViewModel;
import com.blito.rest.viewmodels.account.UserSimpleViewModel;
import com.blito.rest.viewmodels.account.UserViewModel;

@Service
public class AdminService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	UserMapper userMapper;

	public Page<UserSimpleViewModel> getAllUsers(Pageable pageable) {
		return userMapper.toPage(userRepository.findAll(pageable), userMapper::userToUserSimpleViewModel);
	}

	public UserViewModel getUser(long userId) {
		User user = userRepository.findById(userId).map(u -> u)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.USER_NOT_FOUND)));
		return userMapper.createFromEntity(user);
	}

	public UserViewModel updateUser(UserAdminUpdateViewModel vmodel) {
		User user = userRepository.findById(vmodel.getUserId()).map(u -> u)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.USER_NOT_FOUND)));
		userRepository.save(userMapper.userAdminUpdateViewModelToUser(vmodel, user));
		return userMapper.createFromEntity(user);
	}								
	
}
