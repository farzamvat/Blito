package com.blito.rest.viewmodels;

public class LocationViewModel {
	private Long profile_id;
	private double latitude;
	private double longitude;
	private Double distance;
	
	public LocationViewModel(double latitude,double longitude)
	{
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public LocationViewModel(Long profile_id, Double distance) {
		this.profile_id = profile_id;
		this.distance = distance;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Long getProfile_id() {
		return profile_id;
	}

	public void setProfile_id(Long profile_id) {
		this.profile_id = profile_id;
	}

	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "LocationViewModel{" +
				"profile_id=" + profile_id +
				", latitude=" + latitude +
				", longitude=" + longitude +
				", distance=" + distance +
				'}';
	}
}
