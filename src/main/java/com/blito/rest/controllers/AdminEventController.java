package com.blito.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.blito.enums.Response;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.View;
import com.blito.rest.viewmodels.adminreport.BlitBuyerViewModel;
import com.blito.rest.viewmodels.event.AdminChangeEventOperatorStateVm;
import com.blito.rest.viewmodels.event.AdminChangeEventStateVm;
import com.blito.rest.viewmodels.event.EventFlatViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.exception.ExceptionViewModel;
import com.blito.services.AdminEventService;
import com.blito.services.ExcelService;
import com.blito.view.ExcelView;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("${api.base.url}" + "/admin/events")
public class AdminEventController {
	@Autowired AdminEventService adminEventService;
	@Autowired ExcelService excelService;
	
	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "change event state")
	@ApiResponses({ @ApiResponse(code = 200, message = "change event state ok", response = ResultVm.class),
					@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@PutMapping("/change-event-state")
	public ResponseEntity<ResultVm> changeEventState(AdminChangeEventStateVm vmodel){
		adminEventService.changeEventState(vmodel);
		return ResponseEntity.ok(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}
	
	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "change event operator state")
	@ApiResponses({ @ApiResponse(code = 200, message = "change event operator state ok", response = ResultVm.class),
					@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@PutMapping("/change-event-operator-state")
	public ResponseEntity<ResultVm> changeEventOperatorState(AdminChangeEventOperatorStateVm vmodel){
		adminEventService.changeOperatorState(vmodel);
		return ResponseEntity.ok(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "set event order number")
	@ApiResponses({ @ApiResponse(code = 200, message = "set event order number ok", response = ResultVm.class),
					@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@PutMapping("/set-event-order-number/{eventId}")
	public ResponseEntity<ResultVm> setEvetOrderNumvber(@PathVariable long eventId, @RequestParam int order ){
		adminEventService.setEventOrderNumber(eventId, order);
		return ResponseEntity.ok(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}
	
	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "get all events")
	@ApiResponses({ @ApiResponse(code = 200, message = "get all events ok", response = EventFlatViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@JsonView(View.SimpleEvent.class)
	@GetMapping("/all")
	public ResponseEntity<Page<EventFlatViewModel>> getAllEvents(Pageable page){
		return ResponseEntity.ok(adminEventService.getAllEvents(page));
	}
	
	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "get event")
	@ApiResponses({ @ApiResponse(code = 200, message = "get event ok", response = EventFlatViewModel.class),
					@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@JsonView(View.AdminEvent.class)
	@GetMapping
	public ResponseEntity<EventFlatViewModel> getEvent(@RequestParam long eventId){
		return ResponseEntity.ok(adminEventService.getEvent(eventId));
	}
	
	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "update event")
	@ApiResponses({ @ApiResponse(code = 200, message = "update event ok", response = EventFlatViewModel.class),
					@ApiResponse(code = 400, message = "ValidatonException", response = ExceptionViewModel.class),
					@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@JsonView(View.AdminEvent.class)
	@PutMapping
	public ResponseEntity<EventFlatViewModel> updateEvent(@Validated EventViewModel vmodel){
		return ResponseEntity.ok(adminEventService.updateEvent(vmodel));
	}
	
	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "get event blit buyers by date")
	@ApiResponses({ @ApiResponse(code = 200, message = "get event blit buyers by datee ok", response = BlitBuyerViewModel.class),
					@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@GetMapping("/get-event-blit-buyers-by-date")
	public ResponseEntity<Page<BlitBuyerViewModel>> getEventBlitBuyers(@RequestParam long eventDateId, Pageable page){
		return ResponseEntity.ok(adminEventService.getEventBlitBuyersByEventDate(eventDateId, page));
	}
	
	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "get event blit buyers by date excel")
	@ApiResponses({ @ApiResponse(code = 200, message = "get event blit buyers by datee excel ok", response = ModelAndView.class),
					@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@GetMapping("/get-event-blit-buyers-by-date.xls")
	public ModelAndView getEventBlitBuyersExcel(@RequestParam long eventDateId) {
		return new ModelAndView(new ExcelView(), excelService.getBlitBuyersMap(eventDateId));
	}
}
