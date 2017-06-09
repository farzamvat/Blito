package com.blito.rest.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.blito.exceptions.NotAllowedException;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.View;
import com.blito.rest.viewmodels.discount.DiscountViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.exception.ExceptionViewModel;
import com.blito.services.EventService;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("${api.base.url}" + "/events")
public class EventController {

	@Autowired
	EventService eventService;

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "create event")
	@ApiResponses({ @ApiResponse(code = 201, message = "created successfully", response = EventViewModel.class),
			@ApiResponse(code = 400, message = "ValidationException", response = ExceptionViewModel.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@JsonView(View.Event.class)
	@PostMapping
	public ResponseEntity<EventViewModel> create(@Validated @RequestBody EventViewModel vmodel) {
		if(vmodel.getEventDates().stream().flatMap(ed -> ed.getBlitTypes().stream()).anyMatch(bt -> {
			return bt.isFree() ? bt.getPrice() != 0 : bt.getPrice() <= 0;
		})) {
			throw new NotAllowedException("");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(eventService.create(vmodel));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "update event")
	@ApiResponses({ @ApiResponse(code = 202, message = "update event accepted", response = EventViewModel.class),
			@ApiResponse(code = 400, message = "ValidationException or NotAllowedException" 
												+ " or EventLinkAlreadyExistsException", response = ExceptionViewModel.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@JsonView(View.Event.class)
	@PutMapping
	public ResponseEntity<EventViewModel> updateEvent(@Validated @RequestBody EventViewModel vmodel) {
		return ResponseEntity.accepted().body(eventService.update(vmodel));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "delete event")
	@ApiResponses({ @ApiResponse(code = 202, message = "delete event accepted", response = EventViewModel.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class),
			@ApiResponse(code = 400, message = "NotAllowedException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@DeleteMapping("/{eventId}")
	public ResponseEntity<ResultVm> delete(@PathVariable long eventId) {
		eventService.delete(eventId);
		return ResponseEntity.accepted().body(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}
	
	@PostMapping("/set-discount-code")
	public ResponseEntity<DiscountViewModel> setDiscountCode(@Validated @RequestBody DiscountViewModel vmodel) {
		return ResponseEntity.status(HttpStatus.CREATED).body(eventService.setDiscountCode(vmodel));
	}
	
	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "get all user's events")
	@ApiResponses({ @ApiResponse(code = 200, message = "get all user's events ok", response = EventViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@JsonView(View.Event.class)
	@GetMapping("/all-user-events")
	public ResponseEntity<Page<EventViewModel>> getAllUserEvents(Pageable pageable){
		return ResponseEntity.ok(eventService.getUserEvents(pageable));
	}
}
