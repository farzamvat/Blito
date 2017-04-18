package com.blito.rest.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.blito.enums.RegisterEnumValidation;
import com.blito.enums.Response;
import com.blito.exceptions.EmailAlreadyExistsException;
import com.blito.exceptions.ExceptionUtil;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.ExceptionViewModel;
import com.blito.rest.viewmodels.RegisterVm;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.services.UserAccountService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("${api.base.url}")
public class AccountController {
	@Autowired
	UserAccountService userAccountService;

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ExceptionViewModel argumentValidation(HttpServletRequest request,
			MethodArgumentNotValidException exception) {
		return ExceptionUtil.generate(HttpStatus.BAD_REQUEST, request, exception, RegisterEnumValidation.class);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ EmailAlreadyExistsException.class })
	public ExceptionViewModel badRequests(HttpServletRequest request, RuntimeException exception) {
		return ExceptionUtil.generate(HttpStatus.BAD_REQUEST, request, exception);
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "user registration")
	@ApiResponses({ @ApiResponse(code = 201, message = "created successfully", response = ResultVm.class),
			@ApiResponse(code = 400, message = "ValidationException (password is not mathed with confirm password) "
					+ "or EmailAlreadyExistsException", response = ExceptionViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@PostMapping("/register")
	public ResponseEntity<ResultVm> register(@Validated @RequestBody RegisterVm vmodel) {
		if (!vmodel.getPassword().equals(vmodel.getConfirmPassword())) {
			throw new ValidationException(ResourceUtil.getMessage(Response.VALIDATION));
		}
		userAccountService.createUser(vmodel);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResultVm(ResourceUtil.getMessage(Response.REGISTER_SUCCESS)));
	}
}
