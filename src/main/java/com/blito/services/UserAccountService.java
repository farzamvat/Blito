package com.blito.services;

import com.blito.enums.Response;
import com.blito.exceptions.*;
import com.blito.mappers.UserMapper;
import com.blito.models.Blit;
import com.blito.models.Role;
import com.blito.models.User;
import com.blito.repositories.BlitRepository;
import com.blito.repositories.RoleRepository;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.account.*;
import com.blito.security.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Service
public class UserAccountService {
	@Autowired UserRepository userRepository;
	@Autowired UserMapper registerVmToUser;
	@Autowired PasswordEncoder encoder;
	@Autowired MailService mailService;
	@Autowired JwtService jwtService;
	@Autowired UserMapper userMapper;
	@Autowired RoleRepository roleRepository;
	@Autowired BlitRepository blitRepository;
	
	@Transactional
	public CompletableFuture<User> createUser(RegisterVm vmodel)
	{

		Optional<User> result = userRepository.findByEmail(vmodel.getEmail());
		if(result.isPresent())
		{
			throw new AlreadyExistsException(ResourceUtil.getMessage(Response.EMAIL_ALREADY_IN_USE));
		}
		Role userRole = roleRepository.findByName("USER")
				.orElseThrow(() -> new InternalServerException(ResourceUtil.getMessage(Response.INTERNAL_SERVER_ERROR)));
		User user = registerVmToUser.registerViewModeltoUser(vmodel);
		user.setActivationKey(UUID.randomUUID().toString());
		user.setPassword(encoder.encode(user.getPassword()));
		user.getRoles().add(userRole);
		user.setCreatedAt(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()));
		Set<Blit> userBlits = blitRepository.findByCustomerEmail(vmodel.getEmail());
		if(!userBlits.isEmpty())
		{
			userBlits.forEach(blit -> blit.setUser(user));
		}
		return CompletableFuture.completedFuture(user)
				.thenAccept(savedUser -> 
					mailService.sendActivationEmail(savedUser)
				).thenApply((res) ->
					userRepository.save(user));
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
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.USER_NOT_FOUND)));
		if(!user.isActive())
			throw new UserNotActivatedException(ResourceUtil.getMessage(Response.USER_NOT_ACTIVATED));
			
		if(user.isBanned()) {
			throw new NotAllowedException(ResourceUtil.getMessage(Response.USER_IS_BANNED));
		}
		return CompletableFuture.supplyAsync(() -> encoder.matches(vmodel.getPassword(), user.getPassword()))
				.thenCombine(jwtService.generateToken(user.getEmail()), (isMatchedAsyncResult,asyncTokenResult) -> {
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
						User savedUser = userRepository.save(user);
						asyncTokenResult.setRole(savedUser.getRoles().stream().findFirst().get().getName());
						return asyncTokenResult;
					}
					else {
						throw new WrongPasswordException(ResourceUtil.getMessage(Response.INCORRECT_PASSWORD));
					}
				});
	}
	
	@Transactional
	public void forgetPassword(String email)
	{
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.USER_NOT_FOUND)));
		if(user.isBanned())
			throw new NotAllowedException(ResourceUtil.getMessage(Response.USER_IS_BANNED));
		user.setResetKey(RandomUtil.generatePassword());
		user.setPassword(encoder.encode(user.getResetKey()));
		mailService.sendPasswordResetEmail(user);
		user.setResetKey(null);
		userRepository.save(user);
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
		User user = userRepository.findByEmail(jwtService.refreshTokenValidation(refresh_token))
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.USER_NOT_FOUND)));
		return jwtService.generateAccessToken(user.getEmail())
				.thenApply(tokenModel -> {
					tokenModel.setRefreshToken(refresh_token);
					return tokenModel;
				});
	}
	
}
