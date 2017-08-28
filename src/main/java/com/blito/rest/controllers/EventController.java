package com.blito.rest.controllers;

import com.blito.annotations.Permission;
import com.blito.enums.ApiBusinessName;
import com.blito.enums.Response;
import com.blito.enums.validation.ControllerEnumValidation;
import com.blito.exceptions.ExceptionUtil;
import com.blito.models.User;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.utility.HandleUtility;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.View;
import com.blito.rest.viewmodels.discount.DiscountViewModel;
import com.blito.rest.viewmodels.event.ChangeEventStateVm;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.exception.ExceptionViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.EventService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

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
	@Permission(value = ApiBusinessName.USER)
	@JsonView(View.Event.class)
	@PostMapping
	public ResponseEntity<EventViewModel> create(@Validated @RequestBody EventViewModel vmodel) {
		return ResponseEntity.status(HttpStatus.CREATED).body(eventService.create(vmodel));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "update event")
	@ApiResponses({ @ApiResponse(code = 202, message = "update event accepted", response = EventViewModel.class),
			@ApiResponse(code = 400, message = "ValidationException or NotAllowedException"
					+ " or EventLinkAlreadyExistsException", response = ExceptionViewModel.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.USER)
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
	@Permission(value = ApiBusinessName.USER)
	@DeleteMapping("/{eventId}")
	public ResponseEntity<ResultVm> delete(@PathVariable long eventId) {
		eventService.delete(eventId);
		return ResponseEntity.accepted().body(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}

	@Permission(value = ApiBusinessName.USER)
	@PostMapping("/set-discount-code")
	public CompletionStage<ResponseEntity<?>> setDiscountCode(@Valid @RequestBody DiscountViewModel vmodel,
			BindingResult bindingResult, HttpServletRequest req, HttpServletResponse res) {
		if (bindingResult.hasFieldErrors())
			return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(ExceptionUtil
					.generate(HttpStatus.BAD_REQUEST, req, bindingResult, ControllerEnumValidation.class)));
		User user = SecurityContextHolder.currentUser();
		return CompletableFuture.supplyAsync(() -> eventService.setDiscountCode(vmodel,user))
				.handle((either, throwable) -> HandleUtility.generateEitherResponse(either, throwable, req, res));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "get all user's events")
	@ApiResponses({ @ApiResponse(code = 200, message = "get all user's events ok", response = EventViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.USER)
	@JsonView(View.AdminEvent.class)
	@GetMapping("/all-user-events")
	public ResponseEntity<Page<EventViewModel>> getAllUserEvents(Pageable pageable) {
		return ResponseEntity.ok(eventService.getUserEvents(pageable));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "change event state")
	@ApiResponses({ @ApiResponse(code = 200, message = "change event state ok", response = ResultVm.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class),
			@ApiResponse(code = 400, message = "NotAllowedException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.USER)
	@PutMapping("/change-event-state")
	public ResponseEntity<ResultVm> changeEventState(@Validated @RequestBody ChangeEventStateVm vmodel) {
		eventService.changeEventState(vmodel);
		return ResponseEntity.ok(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}

	@Permission(value = ApiBusinessName.USER)
	@DeleteMapping("/{eventId}/{uuid}")
	public CompletionStage<ResponseEntity<?>> deleteGalleryPhoto(@PathVariable long eventId, @PathVariable String uuid,
			HttpServletRequest req, HttpServletResponse res) {

		return CompletableFuture.runAsync(() -> eventService.deleteEventGalleryPhoto(eventId, uuid))
				.handle((result, throwable) -> HandleUtility.generateResponseResult(() -> result, throwable, req, res));
	}
}
