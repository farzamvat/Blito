package com.blito.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.blito.models.EventHost;
import com.blito.rest.viewmodels.eventhost.EventHostSimpleViewModel;
import com.blito.rest.viewmodels.eventhost.EventHostViewModel;

@Component
public class EventHostMapper implements GenericMapper<EventHost,EventHostViewModel> {

	
	public List<EventHostSimpleViewModel> eventHostsToViewModels(List<EventHost> eventHosts)
	{
		return eventHosts.stream().map(e -> eventHostToSimpleViewModel(e)).collect(Collectors.toList());
	}
	
	public EventHostSimpleViewModel eventHostToSimpleViewModel(EventHost eventHost)
	{
		EventHostSimpleViewModel simple = new EventHostSimpleViewModel();
		simple.setEventHostId(eventHost.getEventHostId());
		simple.setHostName(eventHost.getHostName());
		return simple;
	}

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