package com.blito.rest.viewmodels;

import javax.validation.constraints.NotNull;

import com.blito.enums.ImageType;

public class ImageViewModel {
	@NotNull
	String imageUUID;
	@NotNull
	ImageType type;
	
	
	public String getImageUUID() {
		return imageUUID;
	}
	public void setImageUUID(String imageUUID) {
		this.imageUUID = imageUUID;
	}
	public ImageType getType() {
		return type;
	}
	public void setType(ImageType type) {
		this.type = type;
	}
}
