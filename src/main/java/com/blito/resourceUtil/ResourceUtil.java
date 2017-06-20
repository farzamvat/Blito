package com.blito.resourceUtil;

import java.util.Locale;
import java.util.ResourceBundle;

import com.blito.enums.Response;
import com.blito.exceptions.SamanBankException;
import com.blito.validators.ValidationInterface;

public class ResourceUtil {
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages/messages", new Locale("fa"));

	public static String getMessage(Response response) {
		return resourceBundle.getString(response.getMessage());
	}
	
	public static String getMessage(SamanBankException.ResponseStatus status)
	{
		return resourceBundle.getString(status.getErrorMessage());
	}

	public static String getMessage(ValidationInterface iValid) {
		return resourceBundle.getString(iValid.get());
	}

}
