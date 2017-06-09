package com.blito.rest.viewmodels.image;


import org.hibernate.validator.constraints.NotEmpty;

import com.blito.enums.ImageType;
import com.blito.rest.viewmodels.View;
import com.fasterxml.jackson.annotation.JsonView;

public class ImageViewModel {
	@JsonView(View.DefaultView.class)
	@NotEmpty
	String imageUUID;
	@JsonView(View.DefaultView.class)
	@NotEmpty
	ImageType type;
	
	public ImageViewModel() {}
	
	public ImageViewModel(String id,ImageType type)
	{
		this.type = type;
		this.imageUUID = id;
	}
	
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
