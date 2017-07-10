package com.blito.services;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blito.enums.Response;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.IndexBannerMapper;
import com.blito.models.Event;
import com.blito.models.Image;
import com.blito.models.IndexBanner;
import com.blito.repositories.EventRepository;
import com.blito.repositories.ImageRepository;
import com.blito.repositories.IndexBannerRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.BannerViewModel;

@Service
public class IndexBannerService {

	@Autowired
	IndexBannerMapper indexBannerMapper;
	@Autowired
	ImageRepository imageRepository;
	@Autowired
	EventRepository eventRepository;
	@Autowired
	IndexBannerRepository indexBannerRepository;
	@Autowired
	ImageService imageService;

	@Transactional
	public BannerViewModel create(BannerViewModel vmodel) {
		IndexBanner indexBanner = indexBannerMapper.createFromViewModel(vmodel);
		indexBanner = setIndexBannerImageAndEvent(indexBanner,vmodel);
		return indexBannerMapper.createFromEntity(indexBannerRepository.save(indexBanner));
	}

	@Transactional
	public CompletableFuture<?> update(BannerViewModel vmodel) {
		IndexBanner indexBanner = Optional.ofNullable(indexBannerRepository.findOne(vmodel.getIndexBannerId()))
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.INDEX_BANNER_NOT_FOUND)));
		if(!vmodel.getImage().getImageUUID().equals(indexBanner.getImage().getImageUUID()))
		{
			return imageService.delete(indexBanner.getImage().getImageUUID());
		}
		indexBanner = indexBannerMapper.updateEntity(vmodel, indexBanner);
		indexBanner = setIndexBannerImageAndEvent(indexBanner,vmodel);
		return CompletableFuture.completedFuture(indexBannerMapper.createFromEntity(indexBanner));
	}
	
	private IndexBanner setIndexBannerImageAndEvent(IndexBanner indexBanner,BannerViewModel vmodel)
	{
		Image image = imageRepository.findByImageUUID(vmodel.getImage().getImageUUID())
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND)));
		image.setImageType(vmodel.getImage().getType().name());
		indexBanner.setImage(image);
		Event event = eventRepository.findByEventLinkAndIsDeletedFalse(vmodel.getEventLink())
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));
		indexBanner.setEvent(event);
		return indexBanner;
	}
	
	public CompletableFuture<Page<BannerViewModel>> getIndexBanners(Pageable pageable)
	{
		return CompletableFuture.supplyAsync(() -> indexBannerMapper.toPage(indexBannerRepository.findAll(pageable)));
	}
	
	public CompletableFuture<?> delete(long indexBannerId)
	{
		return CompletableFuture.runAsync(() -> {
			Optional<IndexBanner> result = Optional.ofNullable(indexBannerRepository.findOne(indexBannerId));
			if(result.isPresent())
				indexBannerRepository.delete(indexBannerId);
			else
				throw new NotFoundException(ResourceUtil.getMessage(Response.INDEX_BANNER_NOT_FOUND));
		});
	}
}
