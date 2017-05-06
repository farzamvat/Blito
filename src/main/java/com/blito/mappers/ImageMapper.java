package com.blito.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.blito.enums.Response;
import com.blito.exceptions.NotFoundException;
import com.blito.models.Image;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.image.ImageViewModel;

@Component
public class ImageMapper implements GenericMapper<Image,ImageViewModel> {
	
	public List<Image> setImageTypeFromImageViewModels(List<Image> images,List<ImageViewModel> vmodels)
	{
		return images.stream().map(im -> vmodels.stream()
				.filter(imv -> imv.getImageUUID().equals(im.getImageUUID())).map(imageViewModel -> {
					im.setImageType(imageViewModel.getType());
					return im;
				}).findFirst().map(i -> i)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND))))
				.collect(Collectors.toList());
	}

	@Override
	public Image createFromViewModel(ImageViewModel viewModel) {
		return null;
	}

	@Override
	public ImageViewModel createFromEntity(Image image) {
		ImageViewModel vmodel = new ImageViewModel();
		vmodel.setImageUUID(image.getImageUUID());
		vmodel.setType(image.getImageType());
		return vmodel;
	}

	@Override
	public Image updateEntity(ImageViewModel viewModel, Image entity) {
		return null;
	}
}
