package com.blito.models;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.blito.enums.EventType;

@Entity(name="event")
public class Event {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private String eventId;
	
	@OneToMany(mappedBy="event")
	@JoinTable(name="blit_type", joinColumns=@JoinColumn(name="event_id"))
	List<BlitType> blitTypes;
	
	@ManyToOne
	private EventHost eventHost;
	
	
	@Column(name="event_name")
	private String eventName;
	
	@Column(name="event_type")
	@Enumerated(EnumType.STRING)
	private EventType eventType;
	
	@Column(name="blit_sale_start_date")
	private Timestamp blitSaleStartDate;
	
	@Column(name="blit_sale_end_date")
	private Timestamp blitSaleEndDate;
	
	private String address;
	
	private String description;
	
	
	

}
