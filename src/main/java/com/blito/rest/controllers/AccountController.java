package com.blito.rest.controllers;

import java.util.regex.Pattern;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;

import com.blito.annotations.Permission;
import com.blito.configs.Constants;
import com.blito.enums.ApiBusinessName;
import com.blito.enums.Response;
import com.blito.enums.validation.ControllerEnumValidation;
import com.blito.mappers.UserMapper;
import com.blito.models.User;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.View;
import com.blito.rest.viewmodels.account.ChangePasswordViewModel;
import com.blito.rest.viewmodels.account.LoginViewModel;
import com.blito.rest.viewmodels.account.RegisterVm;
import com.blito.rest.viewmodels.account.TokenModel;
import com.blito.rest.viewmodels.account.UserViewModel;
import com.blito.rest.viewmodels.exception.ExceptionViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.UserAccountService;
import com.fasterxml.jackson.annotation.JsonView;

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
	@Autowired
	UserMapper userMapper;
	@Value("${serverAddress}")
	private String serverAddress;

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "user registration")
	@ApiResponses({ @ApiResponse(code = 201, message = "created successfully", response = ResultVm.class),
			@ApiResponse(code = 400, message = "ValidationException"
					+ "or EmailAlreadyExistsException", response = ExceptionViewModel.class),
			@ApiResponse(code = 500, message = "InternalServerException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.USER)
	@PostMapping("/register")
	public DeferredResult<ResponseEntity<ResultVm>> register(@Validated @RequestBody RegisterVm vmodel) {
		if (!vmodel.getPassword().equals(vmodel.getConfirmPassword())) {
			throw new ValidationException(ResourceUtil.getMessage(Response.VALIDATION));
		}
		DeferredResult<ResponseEntity<ResultVm>> deferred = new DeferredResult<>();
		return userAccountService.createUser(vmodel).thenApply(result -> {
			deferred.setResult(ResponseEntity.status(HttpStatus.CREATED)
					.body(new ResultVm(ResourceUtil.getMessage(Response.REGISTER_SUCCESS), true)));
			return deferred;
		}).exceptionally(t -> {
			deferred.setErrorResult(t.getCause());
			return deferred;
		}).join();
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "activate user")
	@ApiResponses({ @ApiResponse(code = 200, message = "", response = ModelAndView.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.USER)
	@GetMapping("/activate")
	public ModelAndView activateAccount(@RequestParam String key, @RequestParam String email) {
		return userRepository.findByEmail(email).map(u -> {
			if (u.getActivationKey() != null) {
				if (u.getActivationKey().equals(key)) {
					u.setActive(true);
					u.setActivationKey(null);
					u = userRepository.save(u);
					return new ModelAndView("activationSuccess").addObject("firstname", u.getFirstname())
							.addObject("serverAddresss", serverAddress);
				} else {
					return new ModelAndView("activationFailed")
							.addObject("message", ResourceUtil.getMessage(Response.INVALID_ACTIVATION_KEY))
							.addObject("serverAddress", serverAddress);
				}
			} else {
				return new ModelAndView("activationFailed")
						.addObject("message", ResourceUtil.getMessage(Response.USER_ALREADY_ACTIVATED))
						.addObject("serverAddress", serverAddress);
			}
		}).orElseGet(() -> {
			return new ModelAndView("activationFailed")
					.addObject("message", ResourceUtil.getMessage(Response.USER_NOT_FOUND))
					.addObject("serverAddress", serverAddress);
		});
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "login")
	@ApiResponses({ @ApiResponse(code = 200, message = "login successful", response = TokenModel.class),
			@ApiResponse(code = 400, message = "Wrong password exception " + "or User not activated exception "
					+ "or ValidationException"),
			@ApiResponse(code = 404, message = "User not found exception", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.USER)
	@PostMapping("/login")
	public DeferredResult<ResponseEntity<TokenModel>> login(@Validated @RequestBody LoginViewModel vmodel) {
		DeferredResult<ResponseEntity<TokenModel>> deferred = new DeferredResult<>();
		return userAccountService.login(vmodel).thenApply(tokenModel -> {
			deferred.setResult(ResponseEntity.ok(tokenModel));
			return deferred;
		}).exceptionally(throwable -> {
			deferred.setErrorResult(throwable.getCause());
			return deferred;
		}).join();
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "change password")
	@ApiResponses({ @ApiResponse(code = 200, message = "password change successful", response = ResultVm.class),
			@ApiResponse(code = 400, message = "ValidationException"
					+ "or Wrong password exception", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.USER)
	@PostMapping("/account/change-password")
	public DeferredResult<ResponseEntity<?>> changePassword(@Validated @RequestBody ChangePasswordViewModel vmodel) {
		if (!vmodel.getConfirmNewPassword().equals(vmodel.getNewPassword())) {
			throw new ValidationException(ResourceUtil.getMessage(Response.VALIDATION));
		}
		DeferredResult<ResponseEntity<?>> deferred = new DeferredResult<>();
		return userAccountService.changePassword(vmodel).thenApply(user -> {
			deferred.setResult(ResponseEntity.accepted().body(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS))));
			return deferred;
		}).exceptionally(throwable -> {
			deferred.setErrorResult(throwable.getCause());
			return deferred;
		}).join();
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "send reset password email")
	@ApiResponses({ @ApiResponse(code = 200, message = "", response = ResultVm.class),
			@ApiResponse(code = 400, message = "ValidationException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.USER)
	@GetMapping("/forget-password")
	public DeferredResult<ResponseEntity<?>> forgetPassword(@RequestParam String email) {
		DeferredResult<ResponseEntity<?>> deferred = new DeferredResult<ResponseEntity<?>>();
		if (!Pattern.compile(Constants.EMAIL_REGEX).matcher(email).matches()) {
			deferred.setErrorResult(new ValidationException(ResourceUtil.getMessage(ControllerEnumValidation.EMAIL)));
			return deferred;
		}
		return userAccountService.forgetPassword(email).thenApply(result -> {
			deferred.setResult(ResponseEntity.accepted()
					.body(new ResultVm(ResourceUtil.getMessage(Response.RESET_PASSWORD_EMAIL_SENT))));
			return deferred;
		}).exceptionally(throwable -> {
			deferred.setErrorResult(throwable.getCause());
			return deferred;
		}).join();
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "get user info")
	@ApiResponses({ @ApiResponse(code = 200, message = "get user info successful", response = UserViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.USER)
	@JsonView(View.SimpleUser.class)
	@GetMapping("/account/user-info")
	public ResponseEntity<UserViewModel> getCurrentUserInfo() {
		User user = userRepository.findOne(SecurityContextHolder.currentUser().getUserId());
		return ResponseEntity.ok(userMapper.createFromEntity(user));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "update user info")
	@ApiResponses({ @ApiResponse(code = 200, message = "update successful", response = UserViewModel.class),
			@ApiResponse(code = 400, message = "ValidationException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.USER)
	@JsonView(View.SimpleUser.class)
	@PostMapping("/account/update-info")
	public ResponseEntity<UserViewModel> updateUserInfo(@Validated @RequestBody UserViewModel vmodel) {
		return ResponseEntity.ok(userAccountService.updateUserInfo(vmodel));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "refresh token")
	@ApiResponses({ @ApiResponse(code = 200, message = "refresh token ok", response = TokenModel.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class),
			@ApiResponse(code = 401, message = "UnauthorizedException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.USER)
	@GetMapping("/refresh")
	public DeferredResult<ResponseEntity<TokenModel>> getAccessToken(@RequestParam String refresh_token) {
		DeferredResult<ResponseEntity<TokenModel>> deferred = new DeferredResult<>();
		return userAccountService.getNewAccessToken(refresh_token).thenApply(result -> {
			deferred.setResult(ResponseEntity.ok(result));
			return deferred;
		}).exceptionally(throwable -> {
			deferred.setErrorResult(throwable.getCause());
			return deferred;
		}).join();
	}
}
