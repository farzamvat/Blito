package com.blito.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blito.enums.Response;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.IndexBannerMapper;
import com.blito.models.Event;
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
	IndexBannerRepository indexBanenrRepository;

	@Transactional
	public BannerViewModel create(BannerViewModel vmodel) {
		IndexBanner indexBanner = indexBannerMapper.createFromViewModel(vmodel);
		indexBanner.setImage(imageRepository.findByImageUUID(vmodel.getImage().getImageUUID())
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND))));
		Event event = Optional.ofNullable(eventRepository.findOne(vmodel.getEventId()))
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));
		indexBanner.setEvent(event);
		return indexBannerMapper.createFromEntity(indexBanenrRepository.save(indexBanner));
	}
}
