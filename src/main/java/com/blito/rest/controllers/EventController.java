package com.blito.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blito.rest.viewmodels.event.EventCreateViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.services.EventService;

@RestController
@RequestMapping("${api.base.url}" + "/events")
public class EventController {
	
	@Autowired EventService eventService;
	
	@PostMapping
	public ResponseEntity<?> create (EventCreateViewModel vmodel) {
		return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(vmodel));
	}
	
	@GetMapping
	public ResponseEntity<EventViewModel> getEvent(@RequestParam long eventId) 
	{
		return ResponseEntity.ok(eventService.getEvent(eventId));
	}
	
	@PutMapping
	public ResponseEntity<?> updateEvent(@PathVariable long eventId,@RequestBody String s)
	{
		return null;
	}
	
}
