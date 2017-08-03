package com.blito.rest.utility;

import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.blito.exceptions.AlreadyExistsException;
import com.blito.exceptions.ExceptionUtil;
import com.blito.exceptions.InconsistentDataException;
import com.blito.exceptions.NotAllowedException;
import com.blito.exceptions.NotFoundException;
import com.blito.exceptions.UnauthorizedException;
import com.blito.exceptions.UserNotActivatedException;
import com.blito.exceptions.WrongPasswordException;

public class HandleUtility {
	public static <T> ResponseEntity<?> generateResponseResult(Supplier<T> supplier, Throwable throwable,
			HttpServletRequest req, HttpServletResponse res) {
		if (throwable != null) {
			return generateErrorResult(throwable.getCause(), req, res);
		}
		return ResponseEntity.ok(supplier.get());
	}

	private static ResponseEntity<?> generateErrorResult(Throwable throwable, HttpServletRequest req,
			HttpServletResponse res) {
		if (throwable.equals(NotFoundException.class))
			return ResponseEntity.status(400).body(ExceptionUtil.generate(HttpStatus.NOT_FOUND, req, throwable));
		else if (throwable.equals(UnauthorizedException.class))
			return ResponseEntity.status(401).body(ExceptionUtil.generate(HttpStatus.UNAUTHORIZED, req, throwable));
		else if (throwable.equals(AlreadyExistsException.class) 
				|| throwable.equals(WrongPasswordException.class)
				|| throwable.equals(UserNotActivatedException.class) 
				|| throwable.equals(NotAllowedException.class)
				|| throwable.equals(InconsistentDataException.class))
			return ResponseEntity.status(400).body(ExceptionUtil.generate(HttpStatus.BAD_REQUEST, req, throwable));

		else
			return ResponseEntity.status(400).body(ExceptionUtil.generate(HttpStatus.BAD_REQUEST, req, throwable));
	}
}
