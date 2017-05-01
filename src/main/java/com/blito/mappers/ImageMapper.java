package com.blito.mappers;

import org.springframework.stereotype.Component;

import com.blito.models.Image;
import com.blito.rest.viewmodels.ImageViewModel;

@Component
public class ImageMapper {

	public ImageViewModel imageToImageViewModel(Image image) {
		ImageViewModel vmodel = new ImageViewModel();
		vmodel.setImageUUID(image.getImageUUID());
		vmodel.setType(image.getImageType());
		return vmodel;
	}
}
