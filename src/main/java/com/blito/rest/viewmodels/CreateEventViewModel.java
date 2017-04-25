package com.blito.rest.viewmodels;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import com.blito.annotations.Url;
import com.blito.enums.EventType;
import com.blito.enums.State;

public class CreateEventViewModel {
	@NotNull
	private String EventName;
	@NotNull
	private EventType eventType;
	private Timestamp blitSalteStartDate;
	private Timestamp blitSaleEndDate;
	private String address;
	private String description;
	private Double latitude;
	private Double longitude;
	@Url
	private String eventLink;
	private String indexTitle;
	private String indexDescription;
	private State state;
	private String aparatDisplayCode;
	private long eventHostId;
	
	
}
