package com.blito.models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.blito.enums.ImageType;

@Entity(name="image")
public class Image {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	long imageId;
	
	String imageUUID;
	
	String imageType;

	public Image() {}

	public Image(String imageUUID) {
		this.imageUUID = imageUUID;
	}

	public long getImageId() {
		return imageId;
	}

	public void setImageId(long imageId) {
		this.imageId = imageId;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public String getImageUUID() {
		return imageUUID;
	}

	public void setImageUUID(String imageUUID) {
		this.imageUUID = imageUUID;
	}
}
