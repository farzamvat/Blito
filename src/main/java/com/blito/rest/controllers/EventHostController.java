package com.blito.rest.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.blito.enums.Response;
import com.blito.enums.validation.EventHostControllerEnumValidation;
import com.blito.exceptions.ExceptionUtil;
import com.blito.exceptions.ImageNotFoundException;
import com.blito.exceptions.NotAllowedException;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.EventHostSimpleViewModel;
import com.blito.rest.viewmodels.EventHostViewModel;
import com.blito.rest.viewmodels.ExceptionViewModel;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.services.EventHostService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("${api.base.url}"+"/event-host")
public class EventHostController {
	@Autowired EventHostService eventHostService;
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ExceptionViewModel argumentValidation(HttpServletRequest request,
			MethodArgumentNotValidException exception) {
		return ExceptionUtil.generate(HttpStatus.BAD_REQUEST, request, exception, EventHostControllerEnumValidation.class);
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({ImageNotFoundException.class})
	public ExceptionViewModel notFound(HttpServletRequest request, RuntimeException exception) {
		return ExceptionUtil.generate(HttpStatus.NOT_FOUND, request, exception);
	}
	
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(NotAllowedException.class)
	public ExceptionViewModel notAllowed(HttpServletRequest request, RuntimeException exception) {
		return ExceptionUtil.generate(HttpStatus.UNAUTHORIZED, request, exception);
	}
	
	
	
	// ***************** SWAGGER DOCS ***************** //
		@ApiOperation(value = "create event host")
		@ApiResponses({ @ApiResponse(code = 201, message = "created successfully", response = EventHostViewModel.class),
				@ApiResponse(code = 400, message = "ValidationException", response = ExceptionViewModel.class),
				@ApiResponse(code = 404, message = "Image not found exception", response = ExceptionViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@PostMapping
	public ResponseEntity<EventHostViewModel> createEventHost(@Validated @RequestBody EventHostViewModel vmodel)
	{
		return ResponseEntity.status(HttpStatus.CREATED).body(eventHostService.create(vmodel));
	}
	
	// ***************** SWAGGER DOCS ***************** //
		@ApiOperation(value = "update event host")
		@ApiResponses({ @ApiResponse(code = 202, message = "update accepted", response = EventHostViewModel.class),
				@ApiResponse(code = 400, message = "ValidationException", response = ExceptionViewModel.class),
				@ApiResponse(code = 404, message = "Image not found exception" + " or EventHostNotFoundException", 
					response = ExceptionViewModel.class),
				@ApiResponse(code = 401, message = "Not allowed exception", response = ExceptionViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@PutMapping
	public ResponseEntity<EventHostViewModel> updateEventHost(@Validated @RequestBody EventHostViewModel vmodel)
	{
		return ResponseEntity.accepted().body(eventHostService.update(vmodel));
	}
	
	// ***************** SWAGGER DOCS ***************** //
		@ApiOperation(value = "get event host")
		@ApiResponses({ @ApiResponse(code = 200, message = "get event host successful", response = EventHostViewModel.class),
				@ApiResponse(code = 404, message = "EventHostNotFoundException", response = ExceptionViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@GetMapping
	public ResponseEntity<EventHostViewModel> getEventHost(@RequestParam long eventHostId)
	{
		return ResponseEntity.ok(eventHostService.get(eventHostId));
	}
	
	// ***************** SWAGGER DOCS ***************** //
		@ApiOperation(value = "delete event host")
		@ApiResponses({ @ApiResponse(code = 202, message = "event host deletion successful", response = EventHostViewModel.class),
				@ApiResponse(code = 404, message = "EventHostNotFoundException", response = ExceptionViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@DeleteMapping
	public ResponseEntity<?> deleteEventHost(@RequestParam long eventHostId)
	{
		eventHostService.delete(eventHostId);
		return ResponseEntity.accepted().body(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}
	
	// ***************** SWAGGER DOCS ***************** //
		@ApiOperation(value = "get all of user's event hosts")
		@ApiResponses({ @ApiResponse(code = 200, message = "get all user's event hosts successful", response = EventHostViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@GetMapping("/all")
	public ResponseEntity<List<EventHostSimpleViewModel>> getCurrentUserEventHosts()
	{
		return ResponseEntity.ok(eventHostService.getCurrentUserEventHosts());
	}
}
