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
import com.blito.services.AdminEventService;
import com.blito.services.ExcelService;
import com.blito.view.ExcelView;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("${api.base.url}" + "/admin/events")
public class AdminEventController {
	@Autowired AdminEventService adminEventService;
	@Autowired ExcelService excelService;
	
	@PutMapping("/change-event-state")
	public ResponseEntity<ResultVm> changeEventState(AdminChangeEventStateVm vmodel){
		adminEventService.changeEventState(vmodel);
		return ResponseEntity.ok(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}
	
	@PutMapping("/change-event-operator-state")
	public ResponseEntity<ResultVm> changeEventOperatorState(AdminChangeEventOperatorStateVm vmodel){
		adminEventService.changeOperatorState(vmodel);
		return ResponseEntity.ok(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}

	@PutMapping("/set-event-order-number/{eventId}")
	public ResponseEntity<ResultVm> setEvetOrderNumvber(@PathVariable long eventId, @RequestParam int order ){
		adminEventService.setEventOrderNumber(eventId, order);
		return ResponseEntity.ok(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}
	
	@JsonView(View.SimpleEvent.class)
	@GetMapping("/all")
	public ResponseEntity<Page<EventFlatViewModel>> getAllEvents(Pageable page){
		return ResponseEntity.ok(adminEventService.getAllEvents(page));
	}
	
	@JsonView(View.Event.class)
	@GetMapping
	public ResponseEntity<EventFlatViewModel> getEvent(@RequestParam long eventId){
		return ResponseEntity.ok(adminEventService.getEvent(eventId));
	}
	
	@JsonView(View.Event.class)
	@PutMapping
	public ResponseEntity<EventFlatViewModel> updateEvent(@Validated EventFlatViewModel vmodel){
		return ResponseEntity.ok(adminEventService.updateEvent(vmodel));
	}
	
	@GetMapping("/get-event-blit-buyers-by-date")
	public ResponseEntity<Page<BlitBuyerViewModel>> getEventBlitBuyers(@RequestParam long eventDateId, Pageable page){
		return ResponseEntity.ok(adminEventService.getEventBlitBuyersByEventDate(eventDateId, page));
	}
	
	@GetMapping("/get-event-blit-buyers-by-date.xls")
	public ModelAndView getEventBlitBuyersExcel(@RequestParam long eventDateId) {
		return new ModelAndView(new ExcelView(), excelService.getBlitBuyersMap(eventDateId));
	}
}
