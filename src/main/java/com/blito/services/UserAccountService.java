package com.blito.services;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blito.enums.Response;
import com.blito.exceptions.EmailAlreadyExistsException;
import com.blito.exceptions.InternalServerException;
import com.blito.exceptions.NotFoundException;
import com.blito.exceptions.UnauthorizedException;
import com.blito.exceptions.UserNotActivatedException;
import com.blito.exceptions.WrongPasswordException;
import com.blito.mappers.UserMapper;
import com.blito.models.Role;
import com.blito.models.User;
import com.blito.repositories.RoleRepository;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.account.ChangePasswordViewModel;
import com.blito.rest.viewmodels.account.LoginViewModel;
import com.blito.rest.viewmodels.account.RegisterVm;
import com.blito.rest.viewmodels.account.TokenModel;
import com.blito.rest.viewmodels.account.UserViewModel;
import com.blito.security.SecurityContextHolder;


@Service
public class UserAccountService {
	@Autowired UserRepository userRepository;
	@Autowired UserMapper registerVmToUser;
	@Autowired PasswordEncoder encoder;
	@Autowired MailService mailService;
	@Autowired JwtService jwtService;
	@Autowired UserMapper userMapper;
	@Autowired RoleRepository roleRepository;
	
	public CompletableFuture<Void> createUser(RegisterVm vmodel)
	{

		Optional<User> result = userRepository.findByEmail(vmodel.getEmail());
		if(result.isPresent())
		{
			throw new EmailAlreadyExistsException(ResourceUtil.getMessage(Response.EMAIL_ALREADY_IN_USE));
		}
		Role userRole = roleRepository.findByName("USER")
				.map(r -> r)
				.orElseThrow(() -> new InternalServerException(ResourceUtil.getMessage(Response.INTERNAL_SERVER_ERROR)));
		User user = registerVmToUser.registerViewModeltoUser(vmodel);
		user.setActivationKey(UUID.randomUUID().toString());
		user.setPassword(encoder.encode(user.getPassword()));
		user.getRoles().add(userRole);
		return CompletableFuture.completedFuture(userRepository.save(user))
				.thenAcceptAsync(savedUser -> 
					mailService.sendActivationEmail(savedUser)
				);
	}
	
	@Transactional
	public UserViewModel updateUserInfo(UserViewModel vmodel)
	{
		User user = userRepository.findOne(SecurityContextHolder.currentUser().getUserId());
		user = userMapper.updateEntity(vmodel, user);
		return userMapper.createFromEntity(user);
	}
	
	@Transactional
	public CompletableFuture<TokenModel> login(LoginViewModel vmodel)
	{
		User user = userRepository.findByEmail(vmodel.getEmail())
				.map(u -> u)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.USER_NOT_FOUND)));
		if(!user.isActive())
			throw new UserNotActivatedException(ResourceUtil.getMessage(Response.USER_NOT_ACTIVATED));
			
		return CompletableFuture.supplyAsync(() -> encoder.matches(vmodel.getPassword(), user.getPassword()))
				.thenCombine(jwtService.generateToken(user.getUserId()), (isMatchedAsyncResult,asyncTokenResult) -> {
					if(isMatchedAsyncResult)
					{
						if(user.isFirstTimeLogin())
						{
							asyncTokenResult.setFirstTime(true);
							user.setFirstTimeLogin(false);
						}
						else {
							asyncTokenResult.setFirstTime(false);
						}
						user.setRefreshToken(asyncTokenResult.getRefreshToken());
						user.setResetKey(null);
						asyncTokenResult.setRole(user.getRoles().stream().findFirst().get().getName());
						return asyncTokenResult;
					}
					else {
						throw new WrongPasswordException(ResourceUtil.getMessage(Response.INCORRECT_PASSWORD));
					}
				});
	}
	
	@Transactional
	public CompletableFuture<Void> forgetPassword(String email)
	{
		User user = userRepository.findByEmail(email)
				.map(u -> u)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.USER_NOT_FOUND)));
		user.setResetKey(RandomUtil.generatePassword());
		user.setPassword(encoder.encode(user.getResetKey()));
		return CompletableFuture.runAsync(() -> mailService.sendPasswordResetEmail(user));
	}
	
	public CompletableFuture<User> changePassword(ChangePasswordViewModel vmodel)
	{
		User user = SecurityContextHolder.currentUser();
		return CompletableFuture.supplyAsync(() -> encoder.matches(vmodel.getOldPassowrd(), user.getPassword()))
				.thenApply(passwordCheck -> {
					if(passwordCheck)
					{
						user.setPassword(encoder.encode(vmodel.getNewPassword()));
						return userRepository.save(user);
					}
					throw new WrongPasswordException(ResourceUtil.getMessage(Response.INCORRECT_PASSWORD));
				});
	}
	
	public CompletableFuture<TokenModel> getNewAccessToken(String refresh_token)
	{
		if(refresh_token == null || refresh_token.equals(""))
			throw new UnauthorizedException(ResourceUtil.getMessage(Response.REFRESH_TOKEN_NOT_PRESENT));
		User user = Optional.ofNullable(userRepository.findOne(jwtService.refreshTokenValidation(refresh_token)))
				.map(u -> u)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.USER_NOT_FOUND)));
		return jwtService.generateAccessToken(user.getUserId())
				.thenApply(tokenModel -> {
					tokenModel.setRefreshToken(refresh_token);
					return tokenModel;
				});
	}
	
}
