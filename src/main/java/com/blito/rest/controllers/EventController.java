package com.blito.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blito.rest.viewmodels.event.EventCreateViewModel;
import com.blito.rest.viewmodels.event.EventUpdateViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.services.EventService;

@RestController
@RequestMapping("${api.base.url}" + "/events")
public class EventController {
	
	@Autowired EventService eventService;
	
	@PostMapping
	public ResponseEntity<?> create (@Validated @RequestBody EventCreateViewModel vmodel) {
		return ResponseEntity.status(HttpStatus.CREATED).body(eventService.create(vmodel));
	}
	
	@GetMapping
	public ResponseEntity<EventViewModel> getEvent(@RequestParam long eventId) 
	{
		return ResponseEntity.ok(eventService.getById(eventId));
	}
	
	@PutMapping
	public ResponseEntity<EventViewModel> updateEvent(@Validated @RequestBody EventUpdateViewModel vmodel)
	{
		return ResponseEntity.accepted().body(eventService.update(vmodel));
	}
	
	
}
