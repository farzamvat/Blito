package com.blito.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.blito.models.Image;
import com.blito.rest.viewmodels.image.ImageViewModel;

@Component
public class ImageMapper implements GenericMapper<Image,ImageViewModel> {
	
	public Set<Image> setImageTypeFromImageViewModels(Set<Image> images,Set<ImageViewModel> vmodels)
	{
		images = images.stream().map(im -> vmodels.stream()
				.filter(imv -> imv.getImageUUID().equals(im.getImageUUID())).map(imageViewModel -> {
					im.setImageType(imageViewModel.getType());
					return im;
				}).findFirst().orElse(null))
				.collect(Collectors.toSet());
		images.removeIf(im -> im == null);
		return images;
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
