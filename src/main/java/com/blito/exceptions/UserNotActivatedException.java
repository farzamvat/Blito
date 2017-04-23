package com.blito.exceptions;

public class UserNotActivatedException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserNotActivatedException(String message)
	{
		super(message);
	}
}
