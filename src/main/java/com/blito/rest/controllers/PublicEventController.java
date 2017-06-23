package com.blito.rest.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blito.mappers.EventFlatMapper;
import com.blito.models.Event;
import com.blito.repositories.EventRepository;
import com.blito.rest.viewmodels.View;
import com.blito.rest.viewmodels.event.EventFlatViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.exception.ExceptionViewModel;
import com.blito.search.SearchViewModel;
import com.blito.services.EventService;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("${api.base.url}" + "/public/events")
public class PublicEventController {
	
	@Autowired EventService eventService;
	@Autowired EventRepository eventRepository;
	@Autowired EventFlatMapper flatMapper;
	

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "get all events")
	@ApiResponses({ @ApiResponse(code = 200, message = "get all event ok", response = EventViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@JsonView(View.SimpleEvent.class)
	@GetMapping("/all")
	public ResponseEntity<Page<EventViewModel>> getAllEvents(Pageable pageable) {
		return ResponseEntity.ok(eventService.getAllEvents(pageable));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "search events")
	@ApiResponses({ @ApiResponse(code = 202, message = "search events ok", response = EventViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@JsonView(View.Event.class)
	@PostMapping("/search")
	public ResponseEntity<?> search(@RequestBody SearchViewModel<Event> searchViewModel, Pageable pageable) {
		Page<EventFlatViewModel> page = eventService.searchEvents(searchViewModel, pageable,flatMapper);
		return ResponseEntity.ok(page);
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "get flat event")
	@ApiResponses({ @ApiResponse(code = 200, message = "get flat event ok", response = EventFlatViewModel.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@JsonView(View.Event.class)
	@GetMapping("/flat/{eventId}")
	public ResponseEntity<EventFlatViewModel> getFlatEvent(@PathVariable long eventId) {
		return ResponseEntity.ok(eventService.getFlatEventById(eventId));
	}
	
	
	@JsonView(View.Event.class)
	@GetMapping("/flat/link/{eventLink}")
	public ResponseEntity<EventFlatViewModel> getFlatEventByLink(@PathVariable String eventLink)
	{
		return ResponseEntity.ok(eventService.getFlatEventByLink(eventLink));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "get event")
	@ApiResponses({ @ApiResponse(code = 200, message = "get event ok", response = EventViewModel.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@JsonView(View.Event.class)
	@GetMapping("/{eventId}")
	public ResponseEntity<EventViewModel> getEvent(@PathVariable long eventId) {
		return ResponseEntity.ok(eventService.getEventById(eventId));
	}

}
