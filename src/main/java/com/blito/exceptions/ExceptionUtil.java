package com.blito.exceptions;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.blito.enums.Response;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.exception.ExceptionViewModel;
import com.blito.rest.viewmodels.exception.FieldErrorViewModel;
import com.blito.validators.ValidationInterface;



public class ExceptionUtil {
	
	public static <T extends Exception> ExceptionViewModel generate(HttpStatus status,
			HttpServletRequest request,
			T exception)
	{
		ExceptionViewModel exvm = new ExceptionViewModel();
		exvm.setError(status.name());
		exvm.setStatus(status.value());
		exvm.setPath(request.getServletPath());
		exvm.setMessage(exception.getMessage());
		exvm.setException(exception.getClass().getName());
		exvm.setTimestamp(System.currentTimeMillis());
		return exvm;
	}
	
	public static <T extends Throwable> ExceptionViewModel generate(HttpStatus status,
			HttpServletRequest request,
			T throwable)
	{
		ExceptionViewModel exvm = new ExceptionViewModel();
		exvm.setError(status.name());
		exvm.setStatus(status.value());
		exvm.setPath(request.getServletPath());
		exvm.setMessage(throwable.getMessage());
		exvm.setException(throwable.getClass().getName());
		exvm.setTimestamp(System.currentTimeMillis());
		return exvm;
	}
	
	public static <T extends Enum<T> & ValidationInterface> ExceptionViewModel generate(HttpStatus status,
			HttpServletRequest request, BindingResult bindingResult, Class<T> clazz)
	{
		ExceptionViewModel exvm = new ExceptionViewModel();
		exvm.setError(status.name());
		exvm.setStatus(status.value());
		exvm.setPath(request.getServletPath());
		exvm.setException(ValidationException.class.getName());
		exvm.setMessage(ResourceUtil.getMessage(Response.VALIDATION));
		exvm.setTimestamp(System.currentTimeMillis());
		T[] validationEnums = clazz.getEnumConstants();
		
		Map<String, T> validationMap = Arrays
				.asList(validationEnums)
				.stream()
				.collect(
						Collectors.toMap(k -> k.toString().toLowerCase(),
								v -> (T) v));
		bindingResult
				.getFieldErrors()
				.stream()
				.forEach(fe -> 
				{
					FieldErrorViewModel field = new FieldErrorViewModel();
					field.setBindingFailure(fe.isBindingFailure());

					if (validationMap.containsKey(fe.getField().toLowerCase())) 
					{
								
						field.setDefaultMessage(
							ResourceUtil.getMessage( validationMap.get(fe.getField().toLowerCase())
							));
					} else {
						field.setDefaultMessage(fe.getDefaultMessage());
					}

					field.setRejectedValue(fe.getRejectedValue());
					field.setField(fe.getField());
					exvm.getErrors().add(field);
				});
		return exvm;
		
	}
	
	public static <T extends Enum<T> & ValidationInterface> ExceptionViewModel generate(HttpStatus status,
			HttpServletRequest request,
			MethodArgumentNotValidException exception, Class<T> clazz)
	{
		ExceptionViewModel exvm = new ExceptionViewModel();
		T[] validationEnums = clazz.getEnumConstants();
		
		
		Map<String, T> validationMap = Arrays
				.asList(validationEnums)
				.stream()
				.collect(
						Collectors.toMap(k -> k.toString().toLowerCase(),
								v -> (T) v));

		
		exvm.setError(status.name());
		exvm.setStatus(status.value());
		exvm.setPath(request.getServletPath());
		exvm.setException(exception.getClass().getName());
		exvm.setMessage(ResourceUtil.getMessage(Response.VALIDATION));
		exvm.setTimestamp(System.currentTimeMillis());
		
		if (exception.getBindingResult().hasFieldErrors()) 
		{
			exception.getBindingResult()
					.getFieldErrors()
					.stream()
					.forEach(fe -> 
					{
						FieldErrorViewModel field = new FieldErrorViewModel();
						field.setBindingFailure(fe.isBindingFailure());

						if (validationMap.containsKey(fe.getField().toLowerCase())) 
						{
									
							field.setDefaultMessage(
								ResourceUtil.getMessage( validationMap.get(fe.getField().toLowerCase())
								));
						} else {
							field.setDefaultMessage(fe.getDefaultMessage());
						}

						field.setRejectedValue(fe.getRejectedValue());
						field.setField(fe.getField());
						exvm.getErrors().add(field);
					});
		}
		return exvm;
	}
}
