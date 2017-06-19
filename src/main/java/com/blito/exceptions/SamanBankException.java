package com.blito.exceptions;

import com.blito.resourceUtil.ResourceUtil;

public class SamanBankException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public static enum ResponseStatus {
		ERROR_NO_1("saman.error.1"),
		ERROR_NO_3("saman.error.3"),
		ERROR_NO_4("saman.error.4"),
		ERROR_NO_6("saman.error.6"),
		ERROR_NO_7("saman.error.7"),
		ERROR_NO_8("saman.error.8"),
		ERROR_NO_9("saman.error.9"),
		ERROR_NO_10("saman.error.10"),
		ERROR_NO_11("saman.error.11"),
		ERROR_NO_12("saman.error.12"),
		ERROR_NO_13("saman.error.13"),
		ERROR_NO_14("saman.error.14"),
		ERROR_NO_15("saman.error.15"),
		ERROR_NO_16("saman.error.16"),
		ERROR_NO_17("saman.error.17"),
		ERROR_NO_18("saman.error.18"),
		ERROR_UNKNOWN("saman.error.unknown");
		
		private String errorMessage;
		
		ResponseStatus(String message)
		{
			this.errorMessage = message;
		}
		
		public String getErrorMessage()
		{
			return this.errorMessage;
		}
	}
	
	public static String getError(String status)
	{
		int responseStatus = Integer.parseInt(status);
		switch (responseStatus) {
		case -1:
			status = getString(ResponseStatus.ERROR_NO_1);
			break;
		case -3:
			status = getString(ResponseStatus.ERROR_NO_3);
			break;
		case -4:
			status = getString(ResponseStatus.ERROR_NO_4);
			break;
		case -6:
			status = getString(ResponseStatus.ERROR_NO_6);
			break;
		case -7:
			status = getString(ResponseStatus.ERROR_NO_7);
			break;
		case -8:
			status = getString(ResponseStatus.ERROR_NO_8);
			break;
		case -9:
			status = getString(ResponseStatus.ERROR_NO_9);
			break;
		case -10:
			status = getString(ResponseStatus.ERROR_NO_10);
			break;
		case -11:
			status = getString(ResponseStatus.ERROR_NO_11);
			break;
		case -12:
			status = getString(ResponseStatus.ERROR_NO_12);
			break;
		case -13:
			status = getString(ResponseStatus.ERROR_NO_13);
			break;
		case -14:
			status = getString(ResponseStatus.ERROR_NO_14);
			break;
		case -15:
			status = getString(ResponseStatus.ERROR_NO_15);
			break;
		case -16:
			status = getString(ResponseStatus.ERROR_NO_16);
			break;
		case -17:
			status = getString(ResponseStatus.ERROR_NO_17);
			break;
		case -18:
			status = getString(ResponseStatus.ERROR_NO_18);
			break;
		default:
			status = getString(ResponseStatus.ERROR_UNKNOWN);
			break;
		}
		return status;
	}

	public SamanBankException(String message)
	{
		super(message);
	}
	
	private static String getString(ResponseStatus status)
	{
		return ResourceUtil.getMessage(status);
	}
}
