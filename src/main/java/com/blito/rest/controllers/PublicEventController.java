package com.blito.rest.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.blito.enums.validation.ControllerEnumValidation;
import com.blito.exceptions.EventLinkAlreadyExistsException;
import com.blito.exceptions.ExceptionUtil;
import com.blito.exceptions.NotAllowedException;
import com.blito.exceptions.NotFoundException;
import com.blito.models.Event;
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
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ExceptionViewModel argumentValidation(HttpServletRequest request,
			MethodArgumentNotValidException exception) {
		return ExceptionUtil.generate(HttpStatus.BAD_REQUEST, request, exception, ControllerEnumValidation.class);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({ NotFoundException.class })
	public ExceptionViewModel notFounds(HttpServletRequest request, RuntimeException exception) {
		return ExceptionUtil.generate(HttpStatus.NOT_FOUND, request, exception);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ NotAllowedException.class, EventLinkAlreadyExistsException.class })
	public ExceptionViewModel notAllowed(HttpServletRequest request, RuntimeException exception) {
		return ExceptionUtil.generate(HttpStatus.BAD_REQUEST, request, exception);
	}

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
	@JsonView(View.SimpleEvent.class)
	@PostMapping("/search")
	public ResponseEntity<?> search(@RequestBody SearchViewModel<Event> searchViewModel, Pageable pageable) {
		return ResponseEntity.ok(eventService.searchEvents(searchViewModel, pageable));
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
