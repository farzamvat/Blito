package com.blito.rest.controllers.account;

import com.blito.annotations.Permission;
import com.blito.configs.Constants;
import com.blito.enums.ApiBusinessName;
import com.blito.enums.Response;
import com.blito.enums.validation.ControllerEnumValidation;
import com.blito.mappers.UserMapper;
import com.blito.models.User;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.utility.HandleUtility;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.View;
import com.blito.rest.viewmodels.account.*;
import com.blito.rest.viewmodels.exception.ExceptionViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.UserAccountService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.regex.Pattern;

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
	@GetMapping("/activate")
	public ModelAndView activateAccount(@RequestParam String key, @RequestParam String email) {
		return userRepository.findByEmail(email).map(u -> {
			if (u.getActivationKey() != null) {
				if (u.getActivationKey().equals(key)) {
					u.setActive(true);
					u.setActivationKey(null);
					u = userRepository.save(u);
					return new ModelAndView("activationSuccess").addObject("firstname", u.getFirstname())
							.addObject("serverAddress", serverAddress);
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
	@GetMapping("/forget-password")
	public CompletionStage<ResponseEntity<?>> forgetPassword(@RequestParam String email, HttpServletRequest req,
			HttpServletResponse res) {
		if (!Pattern.compile(Constants.EMAIL_REGEX).matcher(email).matches())
			throw new ValidationException(ResourceUtil.getMessage(ControllerEnumValidation.EMAIL));
		return CompletableFuture.runAsync(() -> userAccountService.forgetPassword(email))
				.handle((result, throwable) -> HandleUtility.generateResponseResult(
						() -> new ResultVm(ResourceUtil.getMessage(Response.RESET_PASSWORD_EMAIL_SENT)), throwable, req,
						res));
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
	@GetMapping("/refresh")
	public CompletionStage<ResponseEntity<?>> getAccessToken(@RequestParam String refresh_token,
																	  HttpServletRequest request,
																	  HttpServletResponse response) {
		return userAccountService.getNewAccessToken(refresh_token)
				.handle((tokenModel,throwable) -> HandleUtility.generateResponseResult(() -> tokenModel,throwable,request,response));
	}
}
