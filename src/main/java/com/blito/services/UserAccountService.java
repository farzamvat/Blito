package com.blito.services;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blito.enums.Response;
import com.blito.exceptions.EmailAlreadyExistsException;
import com.blito.exceptions.UserNotFoundException;
import com.blito.exceptions.WrongPasswordException;
import com.blito.mappers.UserMapper;
import com.blito.models.User;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.ChangePasswordViewModel;
import com.blito.rest.viewmodels.LoginViewModel;
import com.blito.rest.viewmodels.RegisterVm;
import com.blito.rest.viewmodels.TokenModel;
import com.blito.security.SecurityContextHolder;


@Service
public class UserAccountService {
	@Autowired UserRepository userRepository;
	@Autowired UserMapper registerVmToUser;
	@Autowired PasswordEncoder encoder;
	@Autowired MailService mailService;
	@Autowired JwtService jwtService;
	
	public CompletableFuture<Void> createUser(RegisterVm vmodel)
	{

		Optional<User> result = userRepository.findByEmail(vmodel.getEmail());
		if(result.isPresent())
		{
			throw new EmailAlreadyExistsException(ResourceUtil.getMessage(Response.EMAIL_ALREADY_IN_USE));
		}
		User user = registerVmToUser.registerViewModeltoUser(vmodel);
		user.setActivationKey(UUID.randomUUID().toString());
		user.setPassword(encoder.encode(user.getPassword()));
		return CompletableFuture.completedFuture(userRepository.save(user))
				.thenAcceptAsync(savedUser -> 
					mailService.sendActivationEmail(savedUser)
				);
	}
	
	public CompletableFuture<TokenModel> login(LoginViewModel vmodel)
	{
		User user = userRepository.findByEmail(vmodel.getEmail())
				.map(u -> u)
				.orElseThrow(() -> new UserNotFoundException(ResourceUtil.getMessage(Response.USER_NOT_FOUND)));
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
						userRepository.save(user);
						return asyncTokenResult;
					}
					else {
						throw new WrongPasswordException(ResourceUtil.getMessage(Response.INCORRECT_PASSWORD));
					}
				});
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
	
}
