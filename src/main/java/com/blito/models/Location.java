package com.blito.models;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;


@Entity(name="location")
@PrimaryKeyJoinColumn(referencedColumnName="profileId")
public class Location extends Profile {
	String address;
	
	Double longitude;
	Double latitude;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
}
