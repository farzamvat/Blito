package com.blito.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blito.enums.Response;
import com.blito.exceptions.EventHostNotFoundException;
import com.blito.exceptions.NotAllowedException;
import com.blito.mappers.EventHostMapper;
import com.blito.mappers.ImageMapper;
import com.blito.models.EventHost;
import com.blito.models.Image;
import com.blito.models.User;
import com.blito.repositories.EventHostRepository;
import com.blito.repositories.ImageRepository;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.eventhost.EventHostSimpleViewModel;
import com.blito.rest.viewmodels.eventhost.EventHostViewModel;
import com.blito.security.SecurityContextHolder;

@Service
public class EventHostService {
	@Autowired EventHostMapper eventHostMapper;
	@Autowired ImageRepository imageRepository;
	@Autowired UserRepository userRepository;
	@Autowired EventHostRepository eventHostRepository;
	@Autowired ImageMapper imageMapper;
	
	public EventHostViewModel create(EventHostViewModel vmodel)
	{

		List<Image> images = imageRepository.findByImageUUIDIn(
				vmodel.getImages().stream().map(iv -> iv.getImageUUID()).collect(Collectors.toList()));
		images = imageMapper.setImageTypeFromImageViewModels(images, vmodel.getImages());
		EventHost eventHost = new EventHost();
		eventHost = eventHostMapper.eventHostViewModelToEventHost(vmodel,eventHost);
		eventHost.setImages(images);
		User user = userRepository.findOne(SecurityContextHolder.currentUser().getUserId());
		user.setEventHosts(Arrays.asList(eventHost));
		return eventHostMapper.eventHostToViewModel(eventHost);
	}
	
	public EventHostViewModel update(EventHostViewModel vmodel)
	{
		EventHost eventHost = Optional.ofNullable(eventHostRepository.findOne(vmodel.getEventHostId()))
				.map(e -> e)
				.orElseThrow(() -> new EventHostNotFoundException(ResourceUtil.getMessage(Response.EVENT_HOST_NOT_FOUND)));
		if(eventHost.getUser().getUserId() != SecurityContextHolder.currentUser().getUserId())
		{
			throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
		}
		List<Image> images = imageRepository.findByImageUUIDIn(
				vmodel.getImages().stream().map(iv -> iv.getImageUUID()).collect(Collectors.toList()));
		images = imageMapper.setImageTypeFromImageViewModels(images, vmodel.getImages());
		eventHost.setImages(images);
		eventHost = eventHostMapper.eventHostViewModelToEventHost(vmodel,eventHost);
		
		return eventHostMapper.eventHostToViewModel(eventHost);
	}
	
	public EventHostViewModel get(long id)
	{
		EventHost eventHost = Optional.ofNullable(eventHostRepository.findOne(id))
				.map(e -> e)
				.orElseThrow(() -> new EventHostNotFoundException(ResourceUtil.getMessage(Response.EVENT_HOST_NOT_FOUND)));
		return eventHostMapper.eventHostToViewModel(eventHost);
	}
	
	public void delete(long id)
	{
		EventHost eventHost = Optional.ofNullable(eventHostRepository.findOne(id))
				.map(e -> e)
				.orElseThrow(() -> new EventHostNotFoundException(ResourceUtil.getMessage(Response.EVENT_HOST_NOT_FOUND)));
		eventHostRepository.delete(eventHost);
	}
	
	public List<EventHostSimpleViewModel> getCurrentUserEventHosts()
	{
		User user = userRepository.findOne(SecurityContextHolder.currentUser().getUserId());
		return eventHostMapper.eventHostsToViewModels(user.getEventHosts());
	}
}
