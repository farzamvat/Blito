package com.blito.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blito.configs.Constants;
import com.blito.enums.ImageType;
import com.blito.enums.Response;
import com.blito.exceptions.AlreadyExistsException;
import com.blito.exceptions.NotAllowedException;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.EventHostMapper;
import com.blito.mappers.ImageMapper;
import com.blito.models.EventHost;
import com.blito.models.Image;
import com.blito.models.User;
import com.blito.repositories.EventHostRepository;
import com.blito.repositories.ImageRepository;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.eventhost.EventHostViewModel;
import com.blito.rest.viewmodels.image.ImageViewModel;
import com.blito.search.SearchViewModel;
import com.blito.security.SecurityContextHolder;

@Service
public class EventHostService {
	@Autowired
	EventHostMapper eventHostMapper;
	@Autowired
	ImageRepository imageRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	EventHostRepository eventHostRepository;
	@Autowired
	ImageMapper imageMapper;
	@Autowired
	SearchService searchService;

	private EventHost findEventHostById(long id) {
		return eventHostRepository.findByEventHostIdAndIsDeletedFalse(id).map(e -> e)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_HOST_NOT_FOUND)));
	}

	@Transactional
	public EventHostViewModel create(EventHostViewModel vmodel) {
		Optional<EventHost> result = eventHostRepository.findByHostName(vmodel.getHostName());
		if(result.isPresent())
		{
			throw new AlreadyExistsException(ResourceUtil.getMessage(Response.EVENT_HOST_ALREADY_EXISTS));
		}
		if (vmodel.getImages().stream().filter(i -> i.getType().equals(ImageType.HOST_PHOTO)).count() == 0)
			vmodel.getImages().add(new ImageViewModel(Constants.DEFAULT_HOST_PHOTO, ImageType.HOST_PHOTO));
		if (vmodel.getImages().stream().filter(i -> i.getType().equals(ImageType.HOST_COVER_PHOTO)).count() == 0)
			vmodel.getImages().add(new ImageViewModel(Constants.DEFAULT_HOST_COVER_PHOTO, ImageType.HOST_COVER_PHOTO));
		Set<Image> images = imageRepository.findByImageUUIDIn(
				vmodel.getImages().stream().map(iv -> iv.getImageUUID()).collect(Collectors.toSet()));
		if (images.size() != vmodel.getImages().size()) {
			throw new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND));
		}
		images = imageMapper.setImageTypeFromImageViewModels(images, vmodel.getImages());
		
		User user = userRepository.findOne(SecurityContextHolder.currentUser().getUserId());
		

		EventHost eventHost = eventHostMapper.createFromViewModel(vmodel);
		eventHost.setImages(images);
		eventHost.setUser(user);
		return eventHostMapper.createFromEntity(eventHostRepository.save(eventHost));
	}

	@Transactional
	public EventHostViewModel update(EventHostViewModel vmodel) {
		EventHost eventHost = findEventHostById(vmodel.getEventHostId());
		if (eventHost.getUser().getUserId() != SecurityContextHolder.currentUser().getUserId()) {
			throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
		}
		Set<Image> images = imageRepository.findByImageUUIDIn(
				vmodel.getImages().stream().map(iv -> iv.getImageUUID()).collect(Collectors.toSet()));
		images = imageMapper.setImageTypeFromImageViewModels(images, vmodel.getImages());
		eventHost.setImages(images);
		return eventHostMapper.createFromEntity(eventHostMapper.updateEntity(vmodel, eventHost));
	}

	public EventHostViewModel get(long id) {
		EventHost eventHost = findEventHostById(id);
		if(eventHost.isDeleted()) {
			throw new NotFoundException(ResourceUtil.getMessage(Response.EVENT_HOST_NOT_FOUND));
		}
		return eventHostMapper.createFromEntity(eventHost);
	}

	@Transactional
	public void delete(long id) {
		Optional<EventHost> eventHostResult = eventHostRepository.findByEventHostIdAndIsDeletedFalse(id);
		if (!eventHostResult.isPresent()) {
			throw new NotFoundException(ResourceUtil.getMessage(Response.EVENT_HOST_NOT_FOUND));
		} else {
			if (eventHostResult.get().getUser().getUserId() != SecurityContextHolder.currentUser()
					.getUserId()) {
				throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
			} else {
				eventHostResult.get().setDeleted(true);
			}
		}
	}

	public Page<EventHostViewModel> getAllEventHosts(Pageable pageable) {
		return eventHostMapper.toPage(eventHostRepository.findByIsDeletedFalse(pageable));
	}

	public Page<EventHostViewModel> getCurrentUserEventHosts(Pageable pageable) {
		User user = userRepository.findOne(SecurityContextHolder.currentUser().getUserId());
		return eventHostMapper.toPage(new PageImpl<>(
				eventHostRepository.findByUserUserIdAndIsDeletedFalse(user.getUserId()).stream().skip(pageable.getPageNumber() * pageable.getPageSize())
						.limit(pageable.getPageSize()).collect(Collectors.toList()),
				pageable, user.getEventHosts().size()), eventHostMapper::createFromEntity);
	}

	public Page<EventHostViewModel> searchEventHosts(SearchViewModel<EventHost> searchViewModel, Pageable pageable) {
		return searchService.search(searchViewModel, pageable, eventHostMapper, eventHostRepository);
	}
}
