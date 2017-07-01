package com.blito.exceptions;

public class FileSizeExceedsException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FileSizeExceedsException(String message)
	{
		super(message);
	}
}
