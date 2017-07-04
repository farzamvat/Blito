package com.blito.exceptions;

import com.blito.resourceUtil.ResourceUtil;

public class ZarinpalException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static enum ZarinpalResponseStatus {
		MESSAGE_NO1("zarinpal.message.1"),
		MESSAGE_NO2("zarinpal.message.2"),
		MESSAGE_NO3("zarinpal.message.3"),
		MESSAGE_NO4("zarinpal.message.4"),
		MESSAGE_NO11("zarinpal.message.11"),
		MESSAGE_NO12("zarinpal.message.12"),
		MESSAGE_NO21("zarinpal.message.21"),
		MESSAGE_NO22("zarinpal.message.22"),
		MESSAGE_NO33("zarinpal.message.33"),
		MESSAGE_NO34("zarinpal.message.34"),
		MESSAGE_NO40("zarinpal.message.40"),
		MESSAGE_NO41("zarinpal.message.41"),
		MESSAGE_NO42("zarinpal.message.42"),
		MESSAGE_NO54("zarinpal.message.54"),
		MESSAGE_NO100("zarinpal.message.100"),
		MESSAGE_NO101("zarinpal.message.101");
		
		private String message;
		
		ZarinpalResponseStatus(String message)
		{
			this.message = message;
		}
		
		public String getMessage()
		{
			return message;
		}
	}

	public ZarinpalException(String message)
	{
		super(message);
	}
	
	public static String generateMessage(int status)
	{
		switch (status) {
		case -1:
			return getString(ZarinpalResponseStatus.MESSAGE_NO1);
		case -2:
			return getString(ZarinpalResponseStatus.MESSAGE_NO2);
		case -3:
			return getString(ZarinpalResponseStatus.MESSAGE_NO3);
		case -4:
			return getString(ZarinpalResponseStatus.MESSAGE_NO4);
		case -11:
			return getString(ZarinpalResponseStatus.MESSAGE_NO11);
		case -12:
			return getString(ZarinpalResponseStatus.MESSAGE_NO12);
		case -21:
			return getString(ZarinpalResponseStatus.MESSAGE_NO21);
		case -22:
			return getString(ZarinpalResponseStatus.MESSAGE_NO22);
		case -33:
			return getString(ZarinpalResponseStatus.MESSAGE_NO33);
		case -34:
			return getString(ZarinpalResponseStatus.MESSAGE_NO34);
		case -40:
			return getString(ZarinpalResponseStatus.MESSAGE_NO40);
		case -41:
			return getString(ZarinpalResponseStatus.MESSAGE_NO41);
		case -42:
			return getString(ZarinpalResponseStatus.MESSAGE_NO42);
		case -54:
			return getString(ZarinpalResponseStatus.MESSAGE_NO54);
		case 100:
			return getString(ZarinpalResponseStatus.MESSAGE_NO100);
		case 101:
			return getString(ZarinpalResponseStatus.MESSAGE_NO101);
		default:
			return "Unknown error from zarinpal gateway";
		}
	}
	
	private static String getString(ZarinpalResponseStatus status)
	{
		return ResourceUtil.getMessage(status);
	}
}
