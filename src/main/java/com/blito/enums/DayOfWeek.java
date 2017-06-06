package com.blito.enums;

public enum DayOfWeek {
	SATURDAY("saturday"),
	SUNDAY("sunday"),
	MONDAY("monday"),
	TUESDAY("tuesday"),
	WEDNESDAY("wednesday"),
	THURSDAY("thursday"),
	FRIDAY("friday");
	
	private final String value;
	
	private DayOfWeek(String value)
	{
		this.value=value;
	}
	public String getValue()
	{
		return value;
	}
}
