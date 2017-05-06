package com.blito.exceptions;

public class EventLinkAlreadyExistsException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EventLinkAlreadyExistsException(String message)
	{
		super(message);
	}
}
