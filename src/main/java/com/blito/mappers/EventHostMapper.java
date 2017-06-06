package com.blito.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blito.models.EventHost;
import com.blito.rest.viewmodels.eventhost.EventHostViewModel;

@Component
public class EventHostMapper implements GenericMapper<EventHost,EventHostViewModel> {

	@Autowired
	ImageMapper imageMapper;

	@Override
	public EventHost createFromViewModel(EventHostViewModel vmodel) {
		EventHost eventHost = new EventHost();
		eventHost.setHostName(vmodel.getHostName());
		eventHost.setHostType(vmodel.getHostType());
		eventHost.setInstagramLink(vmodel.getInstagramLink());
		eventHost.setLinkedinLink(vmodel.getLinkedinLink());
		eventHost.setTelegramLink(vmodel.getTelegramLink());
		eventHost.setTwitterLink(vmodel.getTwitterLink());
		eventHost.setWebsiteLink(vmodel.getWebsiteLink());
		eventHost.setTelephone(vmodel.getTelephone());
		return eventHost;
	}

	@Override
	public EventHostViewModel createFromEntity(EventHost eventHost) {
		EventHostViewModel vmodel = new EventHostViewModel();
		vmodel.setEventHostId(eventHost.getEventHostId());
		vmodel.setHostName(eventHost.getHostName());
		vmodel.setHostType(eventHost.getHostType());
		vmodel.setInstagramLink(eventHost.getInstagramLink());
		vmodel.setLinkedinLink(eventHost.getLinkedinLink());
		vmodel.setTelegramLink(eventHost.getTelegramLink());
		vmodel.setTwitterLink(eventHost.getTwitterLink());
		vmodel.setWebsiteLink(eventHost.getWebsiteLink());
		vmodel.setTelephone(eventHost.getTelephone());
		vmodel.setImages(imageMapper.createFromEntities(eventHost.getImages()));
		return vmodel;
	}

	@Override
	public EventHost updateEntity(EventHostViewModel vmodel, EventHost eventHost) {
		eventHost.setHostName(vmodel.getHostName());
		eventHost.setHostType(vmodel.getHostType());
		eventHost.setInstagramLink(vmodel.getInstagramLink());
		eventHost.setLinkedinLink(vmodel.getLinkedinLink());
		eventHost.setTelegramLink(vmodel.getTelegramLink());
		eventHost.setTwitterLink(vmodel.getTwitterLink());
		eventHost.setWebsiteLink(vmodel.getWebsiteLink());
		eventHost.setTelephone(vmodel.getTelephone());
		return eventHost;
	}
}
