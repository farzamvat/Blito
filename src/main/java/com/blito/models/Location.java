package com.blito.models;

import com.vividsolutions.jts.geom.Point;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;


@Entity(name="location")
@PrimaryKeyJoinColumn(referencedColumnName="profileId")
public class Location extends Profile {
	@Column(columnDefinition="TEXT")
	String address;
	
	Double longitude;
	Double latitude;
	@Transient
	private Double distance;

	@Column(name = "location", columnDefinition = "POINT")
	private Point point;

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

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
