package com.blito.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blito.enums.Response;
import com.blito.models.Event;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.View;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.event.EventUpdateViewModel;
import com.blito.rest.viewmodels.event.EventFlatViewModel;
import com.blito.search.SearchViewModel;
import com.blito.services.EventService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("${api.base.url}" + "/events")
public class EventController {
	
	@Autowired EventService eventService;
	
	@JsonView(View.Event.class)
	@PostMapping
	public ResponseEntity<EventViewModel> create (@Validated @RequestBody EventViewModel vmodel) {
		return ResponseEntity.status(HttpStatus.CREATED).body(eventService.create(vmodel));
	}
	
	@JsonView(View.Event.class)
	@GetMapping("/flat/{eventId}")
	public ResponseEntity<EventFlatViewModel> getFlatEvent(@PathVariable long eventId) 
	{
		return ResponseEntity.ok(eventService.getFlatEventById(eventId));
	}
	
	@JsonView(View.Event.class)
	@GetMapping("/{eventId}")
	public ResponseEntity<EventUpdateViewModel> getEvent(@PathVariable long eventId)
	{
		return null;
	}
	
	@JsonView(View.Event.class)
	@PutMapping
	public ResponseEntity<EventViewModel> updateEvent(@Validated @RequestBody EventViewModel vmodel)
	{
		return ResponseEntity.accepted().body(eventService.update(vmodel));
	}
	
	@DeleteMapping
	public ResponseEntity<ResultVm> delete(@PathVariable long eventId)
	{
		eventService.delete(eventId);
		return ResponseEntity.accepted().body(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}
	
	@JsonView(View.SimpleEvent.class)
	@PostMapping("/search")
	public ResponseEntity<?> search(@RequestBody SearchViewModel<Event> searchViewModel,Pageable pageable)
	{
		return ResponseEntity.ok(eventService.searchEvents(searchViewModel, pageable));
	}
}
