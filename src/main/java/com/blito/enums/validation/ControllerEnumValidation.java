package com.blito.enums.validation;

import com.blito.validators.ValidationInterface;

public enum ControllerEnumValidation implements ValidationInterface {
	FIRSTNAME("validation.register.firstname"),
	LASTNAME("validation.register.lastname"),
	EMAIL("validation.register.email"),
	PASSWORD("validation.register.password"),
	MOBILE("validation.register.mobile"),
	EVENT_NAME("validation.create.event.name"),
	EVENT_TYPE("validation.create.event.type"),
	HOSTNAME("validation.eventhost.hostname"),
	TELEPHONE("validation.eventhost.telephone"),
	TELEGRAM_LINK("validation.eventhost.telegram.link"),
	INSTAGRAM_LINK("validation.eventhost.instagram.link"),
	TWITTER_LINK("validation.eventhost.twitter.link"),
	LINKEDIN_LINK("validation.eventhost.linkedin.link"),
	WEBSITE_LINK("validation.eventhost.website.link"),
	HOST_TYPE("validation.eventhost.host.type"),
	TITLE("validation.exchangeblit.title"),
	EVENT_DATE("validation.exchangeblit.event.date"),
	IS_BLITO_EVENT("validation.exchangeblit.is.blito.event"),
	PHONE_NUMBER("validation.exchangeblit.phone.number");
	
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
