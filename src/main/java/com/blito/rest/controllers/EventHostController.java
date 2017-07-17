package com.blito.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.blito.enums.Response;
import com.blito.models.EventHost;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.View;
import com.blito.rest.viewmodels.eventhost.EventHostViewModel;
import com.blito.rest.viewmodels.exception.ExceptionViewModel;
import com.blito.search.SearchViewModel;
import com.blito.services.EventHostService;
import com.blito.view.ExcelView;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("${api.base.url}" + "/event-hosts")
public class EventHostController {
	@Autowired
	EventHostService eventHostService;

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "create event host")
	@ApiResponses({ @ApiResponse(code = 201, message = "created successfully", response = EventHostViewModel.class),
			@ApiResponse(code = 400, message = "ValidationException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@JsonView(View.EventHost.class)
	@PostMapping
	public ResponseEntity<EventHostViewModel> createEventHost(@Validated @RequestBody EventHostViewModel vmodel) {
		return ResponseEntity.status(HttpStatus.CREATED).body(eventHostService.create(vmodel));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "update event host")
	@ApiResponses({ @ApiResponse(code = 202, message = "update accepted", response = EventHostViewModel.class),
			@ApiResponse(code = 400, message = "ValidationException or NotAllowedException", response = ExceptionViewModel.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@JsonView(View.EventHost.class)
	@PutMapping
	public ResponseEntity<EventHostViewModel> updateEventHost(@Validated @RequestBody EventHostViewModel vmodel) {
		return ResponseEntity.accepted().body(eventHostService.update(vmodel));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "get event host")
	@ApiResponses({ @ApiResponse(code = 200, message = "get eventHost ok", response = EventHostViewModel.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@JsonView(View.EventHost.class)
	@GetMapping
	public ResponseEntity<EventHostViewModel> getEventHost(@RequestParam long eventHostId) {
		return ResponseEntity.ok(eventHostService.get(eventHostId));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "delete event host")
	@ApiResponses({ @ApiResponse(code = 202, message = "event host deletion accepted", response = ResultVm.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@DeleteMapping
	public ResponseEntity<?> deleteEventHost(@RequestParam long eventHostId) {
		eventHostService.delete(eventHostId);
		return ResponseEntity.accepted().body(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "get event hosts with excel")
	@ApiResponses({
			@ApiResponse(code = 200, message = "get event hosts with excel ok", response = ModelAndView.class) })
	// ***************** SWAGGER DOCS ***************** //
	@PostMapping("/event-hosts.xlsx")
	public ModelAndView searchUsersForExcel(@RequestBody SearchViewModel<EventHost> search) {
		return new ModelAndView(new ExcelView(), eventHostService.searchEventHostsForExcel(search));
	}
	
	// TODO need refactoring or even removing excel APIs , above and below :)


	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "get all of user's event hosts")
	@ApiResponses({
			@ApiResponse(code = 200, message = "get all user's event hosts successful", response = EventHostViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@JsonView(View.EventHost.class)
	@GetMapping("/all")
	public ResponseEntity<Page<EventHostViewModel>> getCurrentUserEventHosts(Pageable pageable) {
		return ResponseEntity.ok(eventHostService.getCurrentUserEventHosts(pageable));
	}
}
