package com.blito.rest.utility;

import com.blito.enums.Response;
import com.blito.exceptions.*;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.exception.ExceptionViewModel;
import io.vavr.control.Either;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.PessimisticLockException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.function.Supplier;

public class HandleUtility {

	private static final Logger logger = LoggerFactory.getLogger(HandleUtility.class);

	public static <T> ResponseEntity<?> generateResponseResult(Supplier<T> supplier, Throwable throwable,
			HttpServletRequest req, HttpServletResponse res) {
		if (throwable != null) {
			logger.error("Exception e '{}'",throwable.getCause());
			return generateErrorResult(throwable.getCause(), req, res);
		}
		return ResponseEntity.ok(supplier.get());
	}
	public static <T> ResponseEntity<?> generateEitherResponse(Either<ExceptionViewModel,?> either,Throwable throwable,HttpServletRequest req,HttpServletResponse res)
	{
		if(throwable != null) {
			logger.error("Exception e '{}'",throwable.getCause());
			return generateErrorResult(throwable.getCause(), req, res);
		}
		if(either.isLeft())
		{
			either.getLeft().setPath(req.getServletPath());
			return ResponseEntity.status(400).body(either.getLeft());
		}
		else
			return ResponseEntity.ok(either.get());
	}

	private static ResponseEntity<?> generateErrorResult(Throwable throwable, HttpServletRequest req,
			HttpServletResponse res) {
		if (throwable instanceof NotFoundException)
			return ResponseEntity.status(400).body(ExceptionUtil.generate(HttpStatus.NOT_FOUND, req, throwable));
		else if (throwable instanceof UnauthorizedException)
			return ResponseEntity.status(401).body(ExceptionUtil.generate(HttpStatus.UNAUTHORIZED, req, throwable));
		else if (throwable instanceof AlreadyExistsException
				|| throwable instanceof WrongPasswordException
				|| throwable instanceof UserNotActivatedException
				|| throwable instanceof NotAllowedException
				|| throwable instanceof InconsistentDataException)
			return ResponseEntity.status(400).body(ExceptionUtil.generate(HttpStatus.BAD_REQUEST, req, throwable));
		else if (throwable instanceof SeatException) {
			return ResponseEntity.status(400).body(ExceptionUtil.generateSeatError(HttpStatus.BAD_REQUEST,req,(SeatException) throwable));
		}
		else if(throwable instanceof FileNotFoundException)
			return ResponseEntity.status(500).body(ExceptionUtil.generate(HttpStatus.INTERNAL_SERVER_ERROR,req,throwable));
		else if(throwable instanceof PessimisticLockException)
			return ResponseEntity.status(400).body(ExceptionUtil.generate(HttpStatus.BAD_REQUEST,req,new SeatException(ResourceUtil.getMessage(Response.ERROR_PESSIMISTIC_LOCK))));
		else
			return ResponseEntity.status(400).body(ExceptionUtil.generate(HttpStatus.BAD_REQUEST, req, throwable));
	}
}
