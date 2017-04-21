package com.blito.rest.viewmodels;

import javax.validation.constraints.NotNull;

import com.blito.enums.ImageType;

public class ImageViewModel {
	@NotNull
	String encodedBase64;
	@NotNull
	ImageType type;
	public String getEncodedBase64() {
		return encodedBase64;
	}
	public void setEncodedBase64(String encodedBase64) {
		this.encodedBase64 = encodedBase64;
	}
	public ImageType getType() {
		return type;
	}
	public void setType(ImageType type) {
		this.type = type;
	}
}
