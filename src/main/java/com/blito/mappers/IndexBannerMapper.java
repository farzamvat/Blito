package com.blito.mappers;

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
		banner.setActive(viewModel.isActive());
		banner.setDescription(viewModel.getDescription());
		banner.setTitle(viewModel.getTitle());
		return banner;
	}

	@Override
	public BannerViewModel createFromEntity(IndexBanner entity) {
		BannerViewModel vmodel = new BannerViewModel();
		vmodel.setActive(entity.isActive());
		vmodel.setTitle(entity.getTitle());
		vmodel.setDescription(entity.getDescription());
		vmodel.setImage(imageMapper.createFromEntity(entity.getImage()));
		vmodel.setIndexBannerId(entity.getIndexBannerId());
		vmodel.setEventId(entity.getEvent().getEventId());
		return vmodel;
	}

	@Override
	public IndexBanner updateEntity(BannerViewModel viewModel, IndexBanner entity) {
		entity.setActive(viewModel.isActive());
		entity.setDescription(viewModel.getDescription());
		entity.setTitle(viewModel.getTitle());
		return entity;
	}

}
