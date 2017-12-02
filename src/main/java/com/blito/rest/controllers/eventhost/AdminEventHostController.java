package com.blito.rest.controllers.eventhost;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("${api.base.url}" + "/admin/event-hosts")
public class AdminEventHostController {
	
	@Autowired
	private EventHostService eventHostService;
	
	@JsonView(View.AdminEventHost.class)
	@PostMapping("/search")
	public ResponseEntity<Page<EventHostViewModel>> search(@RequestBody SearchViewModel<EventHost> searchViewModel,Pageable pageable)
	{
		return ResponseEntity.ok(eventHostService.searchEventHosts(searchViewModel, pageable));
	}
}
