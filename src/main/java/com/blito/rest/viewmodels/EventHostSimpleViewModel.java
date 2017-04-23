package com.blito.rest.viewmodels;

public class EventHostSimpleViewModel {
	long eventHostId;
	String hostName;
	String imageUUID;
	public long getEventHostId() {
		return eventHostId;
	}
	public void setEventHostId(long eventHostId) {
		this.eventHostId = eventHostId;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getImageUUID() {
		return imageUUID;
	}
	public void setImageUUID(String imageUUID) {
		this.imageUUID = imageUUID;
	}
}
