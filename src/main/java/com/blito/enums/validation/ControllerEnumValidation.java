package com.blito.enums.validation;

import com.blito.validators.ValidationInterface;

public enum ControllerEnumValidation implements ValidationInterface {
	FIRSTNAME("validation.register.firstname"),
	LASTNAME("validation.register.lastname"),
	EMAIL("validation.register.email"),
	PASSWORD("validation.register.password"),
	MOBILE("validation.register.mobile"),
	EVENTNAME("validation.create.event.name"),
	EVENTTYPE("validation.create.event.type"),
	HOSTNAME("validation.eventhost.hostname"),
	TELEPHONE("validation.eventhost.telephone"),
	TELEGRAMLINK("validation.eventhost.telegram.link"),
	INSTAGRAMLINK("validation.eventhost.instagram.link"),
	TWITTERLINK("validation.eventhost.twitter.link"),
	LINKEDINLINK("validation.eventhost.linkedin.link"),
	WEBSITELINK("validation.eventhost.website.link"),
	HOSTTYPE("validation.eventhost.host.type"),
	TITLE("validation.exchangeblit.title"),
	EVENTDATE("validation.exchangeblit.event.date"),
	ISBLITOEVENT("validation.exchangeblit.is.blito.event"),
	PHONENUMBER("validation.exchangeblit.phone.number");
	
	private final String message;
	
	private ControllerEnumValidation(String value)
	{
		this.message = value;
	}
	@Override
	public String get() {
		return this.message;
	}
}
