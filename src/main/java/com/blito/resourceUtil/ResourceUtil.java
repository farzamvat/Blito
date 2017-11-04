package com.blito.resourceUtil;

import java.util.Locale;
import java.util.ResourceBundle;

import com.blito.enums.Response;
import com.blito.enums.SmsMessage;
import com.blito.exceptions.SamanBankException;
import com.blito.exceptions.ZarinpalException;
import com.blito.validators.ValidationInterface;

public class ResourceUtil {
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages/messages", new Locale("fa"));

	public static String getMessage(Response response) {
		return resourceBundle.getString(response.getMessage());
	}
	
	public static String getMessage(SamanBankException.SamanResponseStatus status)
	{
		return resourceBundle.getString(status.getErrorMessage());
	}

	public static String getMessage(ValidationInterface iValid) {
		return resourceBundle.getString(iValid.get());
	}
	
	public static String getMessage(ZarinpalException.ZarinpalResponseStatus status)
	{
		return resourceBundle.getString(status.getMessage());
	}

	public static String getMessage(SmsMessage smsMessage) {
		return resourceBundle.getString(smsMessage.getMessage());
	}

}
