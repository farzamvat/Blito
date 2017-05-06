package com.blito.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blito.enums.Response;
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
	
	private EventHost findEventHostById(long id)
	{
		return Optional.ofNullable(eventHostRepository.findOne(id))
				.map(e -> e)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_HOST_NOT_FOUND)));
	}
	
	public EventHostViewModel create(EventHostViewModel vmodel)
	{

		List<Image> images = imageRepository.findByImageUUIDIn(
				vmodel.getImages().stream().map(iv -> iv.getImageUUID()).collect(Collectors.toList()));
		images = imageMapper.setImageTypeFromImageViewModels(images, vmodel.getImages());
		
		EventHost eventHost = eventHostMapper.createFromViewModel(vmodel);
		eventHost.setImages(images);
		User user = userRepository.findOne(SecurityContextHolder.currentUser().getUserId());
		user.setEventHosts(Arrays.asList(eventHost));
		return eventHostMapper.createFromEntity(eventHost);
	}
	
	public EventHostViewModel update(EventHostViewModel vmodel)
	{
		EventHost eventHost = findEventHostById(vmodel.getEventHostId());
		if(eventHost.getUser().getUserId() != SecurityContextHolder.currentUser().getUserId())
		{
			throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
		}
		List<Image> images = imageRepository.findByImageUUIDIn(
				vmodel.getImages().stream().map(iv -> iv.getImageUUID()).collect(Collectors.toList()));
		images = imageMapper.setImageTypeFromImageViewModels(images, vmodel.getImages());
		eventHost.setImages(images);
		return eventHostMapper.createFromEntity(eventHostRepository.save(eventHostMapper.updateEntity(vmodel,eventHost)));
	}
	
	public EventHostViewModel get(long id)
	{
		return eventHostMapper.createFromEntity(findEventHostById(id));
	}
	
	public void delete(long id)
	{
		EventHost eventHost = findEventHostById(id);
		if(eventHost.getUser().getUserId() != SecurityContextHolder.currentUser().getUserId())
		{
			throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
		}
		eventHostRepository.delete(eventHost);
	}
	
	public List<EventHostSimpleViewModel> getCurrentUserEventHosts()
	{
		User user = userRepository.findOne(SecurityContextHolder.currentUser().getUserId());
		return eventHostMapper.eventHostsToViewModels(user.getEventHosts());
	}
}
