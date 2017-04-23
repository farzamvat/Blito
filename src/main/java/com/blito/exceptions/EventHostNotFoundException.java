package com.blito.exceptions;

public class EventHostNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EventHostNotFoundException(String message)
	{
		super(message);
	}
}
