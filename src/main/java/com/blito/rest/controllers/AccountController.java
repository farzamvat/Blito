package com.blito.rest.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;

import com.blito.enums.Response;
import com.blito.enums.validation.RegisterEnumValidation;
import com.blito.exceptions.EmailAlreadyExistsException;
import com.blito.exceptions.ExceptionUtil;
import com.blito.exceptions.UserNotActivatedException;
import com.blito.exceptions.UserNotFoundException;
import com.blito.exceptions.WrongPasswordException;
import com.blito.mappers.UserMapper;
import com.blito.models.User;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.ChangePasswordViewModel;
import com.blito.rest.viewmodels.ExceptionViewModel;
import com.blito.rest.viewmodels.LoginViewModel;
import com.blito.rest.viewmodels.RegisterVm;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.TokenModel;
import com.blito.rest.viewmodels.UserInfoViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.UserAccountService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("${api.base.url}")
public class AccountController {
	@Autowired
	UserAccountService userAccountService;
	@Autowired
	UserRepository userRepository;
	@Autowired UserMapper userMapper;

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ExceptionViewModel argumentValidation(HttpServletRequest request,
			MethodArgumentNotValidException exception) {
		return ExceptionUtil.generate(HttpStatus.BAD_REQUEST, request, exception, RegisterEnumValidation.class);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ EmailAlreadyExistsException.class, UserNotActivatedException.class, 
		WrongPasswordException.class, ValidationException.class })
	public ExceptionViewModel badRequests(HttpServletRequest request, RuntimeException exception) {
		return ExceptionUtil.generate(HttpStatus.BAD_REQUEST, request, exception);
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({UserNotFoundException.class})
	public ExceptionViewModel notFounds(HttpServletRequest request, RuntimeException exception) {
		return ExceptionUtil.generate(HttpStatus.NOT_FOUND, request, exception);
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "user registration")
	@ApiResponses({ @ApiResponse(code = 201, message = "created successfully", response = ResultVm.class),
			@ApiResponse(code = 400, message = "ValidationException()"
					+ "or EmailAlreadyExistsException", response = ExceptionViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@PostMapping("/register")
	public DeferredResult<ResponseEntity<ResultVm>> register(@Validated @RequestBody RegisterVm vmodel) {
		if (!vmodel.getPassword().equals(vmodel.getConfirmPassword())) {
			throw new ValidationException(ResourceUtil.getMessage(Response.VALIDATION));
		}
		DeferredResult<ResponseEntity<ResultVm>> deferred = new DeferredResult<>();
		return userAccountService.createUser(vmodel).thenApply(result -> {
			deferred.setResult(ResponseEntity.status(HttpStatus.CREATED)
					.body(new ResultVm(ResourceUtil.getMessage(Response.REGISTER_SUCCESS))));
			return deferred;
		}).exceptionally(t -> {
			deferred.setErrorResult(t.getCause());
			return deferred;
		}).join();
	}
	
	
	@GetMapping("/activate")
	public ModelAndView activateAccount(@RequestParam String key)
	{
		return userRepository.findByActivationKey(key)
		.map(u -> {
			u.setActive(true);
			u.setActivationKey(null);
			userRepository.save(u);
			ModelAndView mv = new ModelAndView();
			mv.setViewName("activationSuccess");
			return mv;
		})
		.orElseGet(() -> {
			ModelAndView mv = new ModelAndView();
			mv.setViewName("activationFailed");
			return mv;
		});
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "login")
	@ApiResponses({ @ApiResponse(code = 200, message = "login successful", response = TokenModel.class),
			@ApiResponse(code = 400, message = "Wrong password exception " + "or User not activated exception " +
	"or ValidationException"),
			@ApiResponse(code = 404, message = "User not found exception", response = ExceptionViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@PostMapping("/login")
	public DeferredResult<ResponseEntity<TokenModel>> login(@Validated @RequestBody LoginViewModel vmodel)
	{
		DeferredResult<ResponseEntity<TokenModel>> deferred = new DeferredResult<>();
		return userAccountService.login(vmodel)
				.thenApply(tokenModel -> {
					deferred.setResult(ResponseEntity.ok(tokenModel));
					return deferred;
				})
				.exceptionally(throwable -> {
					deferred.setErrorResult(throwable.getCause());
					return deferred;
				}).join();
	}
	
	// ***************** SWAGGER DOCS ***************** //
		@ApiOperation(value = "change password")
		@ApiResponses({ @ApiResponse(code = 200, message = "password change successful", response = ResultVm.class),
				@ApiResponse(code = 400, message = "ValidationException" +
		"or Wrong password exception", response = ExceptionViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@PostMapping("/account/change-password")
	public DeferredResult<ResponseEntity<?>> changePassword(@Validated @RequestBody ChangePasswordViewModel vmodel)
	{
		if(!vmodel.getConfirmNewPassword().equals(vmodel.getNewPassword()))
		{
			throw new ValidationException(ResourceUtil.getMessage(Response.VALIDATION));
		}
		DeferredResult<ResponseEntity<?>> deferred = new DeferredResult<>();
		return userAccountService.changePassword(vmodel)
				.thenApply(user -> {
					deferred.setResult(ResponseEntity.accepted().body(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS))));
					return deferred;
				})
				.exceptionally(throwable -> {
					deferred.setErrorResult(throwable.getCause());
					return deferred;
				}).join();
	}
	
	// ***************** SWAGGER DOCS ***************** //
		@ApiOperation(value = "get user info")
		@ApiResponses({@ApiResponse(code = 200, message = "get user info successful", response = UserInfoViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@GetMapping("/account/user-info")
	public ResponseEntity<UserInfoViewModel> getCurrentUserInfo()
	{
		return ResponseEntity.ok(userMapper.userToUserInfoViewModel(SecurityContextHolder.currentUser()));
	}
	
	// ***************** SWAGGER DOCS ***************** //
		@ApiOperation(value = "update user info")
		@ApiResponses({ @ApiResponse(code = 200, message = "update successful", response = UserInfoViewModel.class),
			@ApiResponse(code = 400, message = "ValidationException", response = ExceptionViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@PostMapping("/account/update-info")
	public ResponseEntity<UserInfoViewModel> updateUserInfo(@Validated @RequestBody UserInfoViewModel vmodel)
	{
		User user = userMapper.userInfoViewModelToUser(vmodel, SecurityContextHolder.currentUser());
		return ResponseEntity.ok(userMapper.userToUserInfoViewModel(userRepository.save(user)));
	}
	
	
	
}
