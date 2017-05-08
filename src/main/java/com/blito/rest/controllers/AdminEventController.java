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
import com.blito.repositories.EventRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.adminreport.BlitBuyerViewModel;
import com.blito.rest.viewmodels.event.EventSimpleViewModel;
import com.blito.rest.viewmodels.event.EventUpdateViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.services.AdminEventService;
import com.blito.services.ExcelService;
import com.blito.view.ExcelView;

@RestController
@RequestMapping("${api.base.url}" + "/admin/events")
public class AdminEventController {
	@Autowired EventRepository eventRepository;
	@Autowired AdminEventService adminEventService;
	@Autowired ExcelService excelService;
	
	@PutMapping("/close-event")
	public ResponseEntity<?> closeEvent(@RequestParam long eventId){
		adminEventService.closeEvent(eventId);
		return ResponseEntity.ok(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}
	
	@PutMapping("/accept-event")
	public ResponseEntity<?> approveEvent(@RequestParam long eventId){
		adminEventService.approveEvent(eventId);
		return ResponseEntity.ok(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}
	
	@PutMapping("/reject-event")
	public ResponseEntity<?> rejectEvent(@RequestParam long eventId){
		adminEventService.rejectEvent(eventId);
		return ResponseEntity.ok(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}
	
	@PutMapping("/set-event-order-number")
	public ResponseEntity<?> setEvetOrderNumvber(@PathVariable long eventId, @RequestParam int order ){
		adminEventService.setEventOrderNumber(eventId, order);
		return ResponseEntity.ok(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}
	
	@GetMapping("/all")
	public ResponseEntity<Page<EventSimpleViewModel>> getAllEvents(Pageable page){
		return ResponseEntity.ok(adminEventService.getAllEvents(page));
	}
	
	@GetMapping
	public ResponseEntity<EventViewModel> getEvent(@RequestParam long eventId){
		return ResponseEntity.ok(adminEventService.getEvent(eventId));
	}
	
	@PutMapping
	public ResponseEntity<EventViewModel> updateEvent(@Validated EventUpdateViewModel vmodel){
		return ResponseEntity.ok(adminEventService.updateEvent(vmodel));
	}
	
	@GetMapping("/get-event-blit-buyers-by-date")
	public ResponseEntity<Page<BlitBuyerViewModel>> getEventBlitBuyers(@RequestParam long eventDateId, Pageable page){
		return ResponseEntity.ok(adminEventService.getEventBlitBuyersByEventDate(eventDateId, page));
	}
	
	@GetMapping("get-event-blit-buyers-by-date.xls")
	public ModelAndView getEventBlitBuyersExcel(@RequestParam long eventDateId) {
		return new ModelAndView(new ExcelView(), excelService.getBlitBuyersMap(eventDateId));
	}
}
