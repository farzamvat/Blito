package com.blito.exceptions;

public class SendingEmailException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SendingEmailException(String message)
	{
		super(message);
	}
}
