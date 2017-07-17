package com.blito.services;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blito.enums.Response;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.UserMapper;
import com.blito.models.User;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.account.UserViewModel;
import com.blito.search.SearchViewModel;
import com.blito.security.SecurityContextHolder;

@Service
public class AdminAccountService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	UserMapper userMapper;
	@Autowired
	SearchService searchService;
	@Autowired
	ExcelService excelService;

	public UserViewModel getUser(long userId) {
		User user = Optional.ofNullable(userRepository.findOne(userId)).map(u -> u)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.USER_NOT_FOUND)));
		return userMapper.createFromEntity(user);
	}			
	
	public void banUser(long userId) {
		User user = Optional.ofNullable(userRepository.findOne(userId)).map(u->u)
				.orElseThrow(()->new NotFoundException(ResourceUtil.getMessage(Response.USER_NOT_FOUND)));
		user.setBanned(true);
		userRepository.save(user);
	}
	
	public void unBanUser(long userId) {
		User user = Optional.ofNullable(userRepository.findOne(userId)).map(u->u)
				.orElseThrow(()->new NotFoundException(ResourceUtil.getMessage(Response.USER_NOT_FOUND)));
		user.setBanned(false);
		userRepository.save(user);
	}
	
	public Page<UserViewModel> searchUsers(SearchViewModel<User> searchViewModel,Pageable pageable)
	{
		return searchService.search(searchViewModel, pageable, userMapper, userRepository);
	}
	
	public Map<String, Object> searchUsersForExcel(SearchViewModel<User> searchViewModel)
	{
		return excelService.getUserExcelMap(searchService.search(searchViewModel, userMapper, userRepository));
	}
	
	@Transactional
	public UserViewModel updateUserInfo(UserViewModel vmodel)
	{
		User user = userRepository.findOne(vmodel.getUserId());
		user = userMapper.updateEntity(vmodel, user);
		return userMapper.createFromEntity(user);
	}
}
