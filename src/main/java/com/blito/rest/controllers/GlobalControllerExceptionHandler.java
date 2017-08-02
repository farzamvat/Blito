package com.blito.rest.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import com.blito.enums.Response;
import com.blito.enums.validation.ControllerEnumValidation;
import com.blito.exceptions.AlreadyExistsException;
import com.blito.exceptions.ExceptionUtil;
import com.blito.exceptions.FileSizeExceedsException;
import com.blito.exceptions.ForbiddenException;
import com.blito.exceptions.InconsistentDataException;
import com.blito.exceptions.InternalServerException;
import com.blito.exceptions.NotAllowedException;
import com.blito.exceptions.NotFoundException;
import com.blito.exceptions.ResourceNotFoundException;
import com.blito.exceptions.SendingEmailException;
import com.blito.exceptions.UnauthorizedException;
import com.blito.exceptions.UserNotActivatedException;
import com.blito.exceptions.WrongPasswordException;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.exception.ExceptionViewModel;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
	
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ExceptionViewModel argumentValidation(HttpServletRequest request,
			MethodArgumentNotValidException exception) {
		log.error("Method argument validation exception '{}'",exception);
		return ExceptionUtil.generate(HttpStatus.BAD_REQUEST, request, exception, ControllerEnumValidation.class);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ AlreadyExistsException.class, UserNotActivatedException.class,
			WrongPasswordException.class, ValidationException.class, NotAllowedException.class,
			InconsistentDataException.class , SendingEmailException.class})
	public ExceptionViewModel badRequests(HttpServletRequest request, RuntimeException exception) {
		log.error("Logical error in business logic '{}'",exception);
		return ExceptionUtil.generate(HttpStatus.BAD_REQUEST, request, exception);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ NotFoundException.class })
	public ExceptionViewModel notFounds(HttpServletRequest request, RuntimeException exception) {
		log.error("Entity not found exception '{}'",exception);
		return ExceptionUtil.generate(HttpStatus.BAD_REQUEST, request, exception);
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({ ResourceNotFoundException.class })
	public ExceptionViewModel resurceNotFounds(HttpServletRequest request, RuntimeException exception) {
		log.error("Resource not found exception '{}'",exception);
		return ExceptionUtil.generate(HttpStatus.NOT_FOUND, request, exception);
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ InternalServerException.class })
	public ExceptionViewModel internal(HttpServletRequest request, InternalServerException exception) {
		log.error("Internal server error exception '{}'",exception);
		return ExceptionUtil.generate(HttpStatus.INTERNAL_SERVER_ERROR, request, exception);
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(UnauthorizedException.class)
	public ExceptionViewModel unauthorized(HttpServletRequest request, UnauthorizedException exception) {
		log.error("Access denied exception '{}'",exception);
		return ExceptionUtil.generate(HttpStatus.UNAUTHORIZED, request, exception);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MultipartException.class)
	public ExceptionViewModel fileSizeExceeds(HttpServletRequest request,MultipartException exception)
	{
		log.error("File size exceeds limit exception '{}'",exception);
		return ExceptionUtil.generate(HttpStatus.BAD_REQUEST, request, new FileSizeExceedsException(ResourceUtil.getMessage(Response.FILE_UPLOAD_SIZE_EXCEEDS)));
	}
	
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(ForbiddenException.class)
	public ExceptionViewModel forbidden(HttpServletRequest request,ForbiddenException exception)
	{
		log.error("Forbidden exception '{}'",exception);
		return ExceptionUtil.generate(HttpStatus.FORBIDDEN, request, exception);
	}
}
