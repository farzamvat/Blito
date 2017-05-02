package com.blito.enums.validation;

import com.blito.validators.ValidationInterface;

public enum EventHostControllerEnumValidation implements ValidationInterface {
	
	HOSTNAME("validation.eventhost.hostname"),
	TELEPHONE("validation.eventhost.telephone"),
	TELEGRAM_LINK("validation.eventhost.telegram.link"),
	INSTAGRAM_LINK("validation.eventhost.instagram.link"),
	TWITTER_LINK("validation.eventhost.twitter.link"),
	LINKEDIN_LINK("validation.eventhost.linkedin.link"),
	WEBSITE_LINK("validation.eventhost.website.link"),
	HOST_TYPE("validation.eventhost.host.type");
	
	private final String message;
	private EventHostControllerEnumValidation(String value) {
		// TODO Auto-generated constructor stub
		this.message = value;
		
	}

	@Override
	public String get() {
		// TODO Auto-generated method stub
		return this.message;
	}

}
