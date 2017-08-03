package com.blito.mappers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blito.models.IndexBanner;
import com.blito.rest.viewmodels.BannerViewModel;

@Component
public class IndexBannerMapper implements GenericMapper<IndexBanner,BannerViewModel> {
	
	@Autowired
	ImageMapper imageMapper;
	
	@Override
	public IndexBanner createFromViewModel(BannerViewModel viewModel) {
		IndexBanner banner = new IndexBanner();
		banner.setDescription(viewModel.getDescription());
		banner.setTitle(viewModel.getTitle());
		return banner;
	}

	@Override
	public BannerViewModel createFromEntity(IndexBanner entity) {
		BannerViewModel vmodel = new BannerViewModel();
		vmodel.setTitle(entity.getTitle());
		vmodel.setDescription(entity.getDescription());
		vmodel.setImage(imageMapper.createFromEntity(entity.getImage()));
		vmodel.setIndexBannerId(entity.getIndexBannerId());
		Optional.ofNullable(entity.getEvent()).ifPresent(event -> vmodel.setEventLink(event.getEventLink()));
		return vmodel;
	}

	@Override
	public IndexBanner updateEntity(BannerViewModel viewModel, IndexBanner entity) {
		entity.setDescription(viewModel.getDescription());
		entity.setTitle(viewModel.getTitle());
		return entity;
	}

}
