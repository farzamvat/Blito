package com.blito.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blito.models.EventHost;
import com.blito.rest.viewmodels.View;
import com.blito.rest.viewmodels.eventhost.EventHostViewModel;
import com.blito.search.SearchViewModel;
import com.blito.services.EventHostService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("${api.base.url}" + "/public/event-hosts")
public class PublicEventHostController {
	@Autowired EventHostService eventHostService;
	
	@JsonView(View.SimpleEventHost.class)
	@GetMapping
	public ResponseEntity<Page<EventHostViewModel>> getAllEventHosts(Pageable pageable)
	{
		return ResponseEntity.ok(eventHostService.getAllEventHosts(pageable));
	}
	
	@JsonView(View.EventHost.class)
	@GetMapping("/{eventHostId}")
	public ResponseEntity<EventHostViewModel> getEventHostById(@PathVariable long eventHostId)
	{
		return ResponseEntity.ok(eventHostService.get(eventHostId));
	}
	
	@JsonView(View.SimpleEventHost.class)
	@PostMapping("/search")
	public ResponseEntity<Page<EventHostViewModel>> search(@RequestBody SearchViewModel<EventHost> searchViewModel,Pageable pageable)
	{
		return ResponseEntity.ok(eventHostService.searchEventHosts(searchViewModel, pageable));
	}
}
