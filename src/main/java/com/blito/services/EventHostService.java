package com.blito.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blito.enums.Response;
import com.blito.exceptions.EventHostNotFoundException;
import com.blito.exceptions.ImageNotFoundException;
import com.blito.exceptions.NotAllowedException;
import com.blito.mappers.EventHostMapper;
import com.blito.models.EventHost;
import com.blito.models.Image;
import com.blito.models.User;
import com.blito.repositories.EventHostRepository;
import com.blito.repositories.ImageRepository;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.EventHostSimpleViewModel;
import com.blito.rest.viewmodels.EventHostViewModel;
import com.blito.security.SecurityContextHolder;

@Service
public class EventHostService {
	@Autowired EventHostMapper eventHostMapper;
	@Autowired ImageRepository imageRepository;
	@Autowired UserRepository userRepository;
	@Autowired EventHostRepository eventHostRepository;
	
	public EventHostViewModel create(EventHostViewModel vmodel)
	{
		Image image = imageRepository.findByImageUUID(vmodel.getImageUUID())
				.map(i -> i)
				.orElseThrow(() -> new ImageNotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND)));
		EventHost eventHost = new EventHost();
		eventHost = eventHostMapper.eventHostViewModelToEventHost(vmodel,eventHost);
		eventHost.setHostPhoto(image);
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
		Image image = imageRepository.findByImageUUID(vmodel.getImageUUID())
				.map(i -> i)
				.orElseThrow(() -> new ImageNotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND)));
		eventHost = eventHostMapper.eventHostViewModelToEventHost(vmodel,eventHost);
		eventHost.setHostPhoto(image);
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
