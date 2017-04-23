package com.blito.rest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.blito.enums.Response;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.EventHostSimpleViewModel;
import com.blito.rest.viewmodels.EventHostViewModel;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.services.EventHostService;

@RestController
@RequestMapping("${api.base.url}"+"/event-host")
public class EventHostController {
	@Autowired EventHostService eventHostService;

	@PostMapping
	public ResponseEntity<EventHostViewModel> createEventHost(@Validated @RequestBody EventHostViewModel vmodel)
	{
		return ResponseEntity.status(HttpStatus.CREATED).body(eventHostService.create(vmodel));
	}
	
	@PutMapping
	public ResponseEntity<EventHostViewModel> updateEventHost(@Validated @RequestBody EventHostViewModel vmodel)
	{
		return ResponseEntity.accepted().body(eventHostService.update(vmodel));
	}
	
	@GetMapping
	public ResponseEntity<EventHostViewModel> getEventHost(@RequestParam long eventHostId)
	{
		return ResponseEntity.ok(eventHostService.get(eventHostId));
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteEventHost(@RequestParam long eventHostId)
	{
		eventHostService.delete(eventHostId);
		return ResponseEntity.accepted().body(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<EventHostSimpleViewModel>> getCurrentUserEventHosts()
	{
		return ResponseEntity.ok(eventHostService.getCurrentUserEventHosts());
	}
}
