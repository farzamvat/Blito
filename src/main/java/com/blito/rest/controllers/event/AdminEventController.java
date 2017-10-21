package com.blito.rest.controllers.event;

import com.blito.annotations.Permission;
import com.blito.enums.ApiBusinessName;
import com.blito.enums.Response;
import com.blito.mappers.EventMapper;
import com.blito.models.Event;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.View;
import com.blito.rest.viewmodels.adminreport.BlitBuyerViewModel;
import com.blito.rest.viewmodels.blittype.ChangeBlitTypeStateVm;
import com.blito.rest.viewmodels.event.*;
import com.blito.rest.viewmodels.eventdate.ChangeEventDateStateVm;
import com.blito.rest.viewmodels.exception.ExceptionViewModel;
import com.blito.search.SearchViewModel;
import com.blito.services.AdminEventService;
import com.blito.services.ExcelService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.base.url}" + "/admin/events")
public class AdminEventController {
	@Autowired
	AdminEventService adminEventService;
	@Autowired
	ExcelService excelService;
	@Autowired
	EventMapper eventMapper;

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "change event state")
	@ApiResponses({ @ApiResponse(code = 200, message = "change event state ok", response = ResultVm.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.ADMIN)
	@PutMapping("/change-event-state")
	public ResponseEntity<ResultVm> changeEventState(@Validated @RequestBody ChangeEventStateVm vmodel) {
		adminEventService.changeEventState(vmodel);
		return ResponseEntity.ok(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS),true));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "change event date state")
	@ApiResponses({ @ApiResponse(code = 200, message = "change event date state ok", response = ResultVm.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.ADMIN)
	@PutMapping("/change-event-date-state")
	public ResponseEntity<ResultVm> changeEventDateState(@Validated @RequestBody ChangeEventDateStateVm vmodel) {
		adminEventService.changeEventDateState(vmodel);
		return ResponseEntity.ok(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS),true));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "change blit type state")
	@ApiResponses({ @ApiResponse(code = 200, message = "change blit type state ok", response = ResultVm.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.ADMIN)
	@PutMapping("/change-blit-type-state")
	public ResponseEntity<ResultVm> changeBlitTypeState(@Validated @RequestBody ChangeBlitTypeStateVm vmodel) {
		adminEventService.changeBlitTypeState(vmodel);
		return ResponseEntity.ok(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS),true));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "change event operator state")
	@ApiResponses({ @ApiResponse(code = 200, message = "change event operator state ok", response = ResultVm.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.ADMIN)
	@PutMapping("/change-event-operator-state")
	public ResponseEntity<ResultVm> changeEventOperatorState(
			@Validated @RequestBody AdminChangeEventOperatorStateVm vmodel) {
		adminEventService.changeOperatorState(vmodel);
		return ResponseEntity.ok(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "set event as isEvento")
	@ApiResponses({ @ApiResponse(code = 200, message = "set event as isEvento ok", response = ResultVm.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.ADMIN)
	@PutMapping("/set-is-evento")
	public ResponseEntity<ResultVm> setIsEvento(@Validated @RequestBody AdminSetIsEventoViewModel vmodel) {
		adminEventService.setIsEvento(vmodel);
		return ResponseEntity.ok(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "set event offers")
	@ApiResponses({ @ApiResponse(code = 200, message = "set event offers", response = ResultVm.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.ADMIN)
	@PutMapping("/set-event-offers")
	public ResponseEntity<ResultVm> setEventOffers(@Validated @RequestBody AdminChangeOfferTypeViewModel vmodel) {
		adminEventService.setEventOffers(vmodel);
		return ResponseEntity.ok(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}

	@Permission(value = ApiBusinessName.ADMIN)
	@PutMapping("/remove-event-offers")
	public ResponseEntity<ResultVm> removeEventOffers(@Validated @RequestBody AdminChangeOfferTypeViewModel vmodel) {
		adminEventService.removeEventOffers(vmodel);
		return ResponseEntity.ok(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "set event order number")
	@ApiResponses({ @ApiResponse(code = 200, message = "set event order number ok", response = ResultVm.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.ADMIN)
	@PutMapping("/set-event-order-number")
	public ResponseEntity<ResultVm> setEventOrderNumber(@Validated @RequestBody AdminSetEventOrderViewModel vmodel) {
		adminEventService.setEventOrderNumber(vmodel.getEventId(), vmodel.getOrder());
		return ResponseEntity.ok(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "delete event")
	@ApiResponses({ @ApiResponse(code = 200, message = "delete event ok", response = ResultVm.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.ADMIN)
	@DeleteMapping("/{eventId}")
	public ResponseEntity<ResultVm> deleteEvent(@PathVariable long eventId) {
		adminEventService.deleteEvent(eventId);
		return ResponseEntity.ok(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}

	@Permission(value = ApiBusinessName.ADMIN)
	@JsonView(View.AdminEvent.class)
	@PostMapping("/search")
	public ResponseEntity<Page<EventViewModel>> search(@RequestBody SearchViewModel<Event> search, Pageable pageable) {
		return ResponseEntity.ok(adminEventService.searchEvents(search, pageable, eventMapper));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "get event")
	@ApiResponses({ @ApiResponse(code = 200, message = "get event ok", response = EventFlatViewModel.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.ADMIN)
	@JsonView(View.AdminEvent.class)
	@GetMapping("/{eventId}")
	public ResponseEntity<EventFlatViewModel> getFlatEvent(@PathVariable long eventId) {
		return ResponseEntity.ok(adminEventService.getFlatEvent(eventId));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "get event blit buyers by date")
	@ApiResponses({
			@ApiResponse(code = 200, message = "get event blit buyers by datee ok", response = BlitBuyerViewModel.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.ADMIN)
	@GetMapping("/get-event-blit-buyers-by-date")
	public ResponseEntity<Page<BlitBuyerViewModel>> getEventBlitBuyers(@RequestParam long eventDateId, Pageable page) {
		return ResponseEntity.ok(adminEventService.getEventBlitBuyersByEventDate(eventDateId, page));
	}


}
