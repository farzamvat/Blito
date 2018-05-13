package com.blito.rest.controllers.eventhost;

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
import com.blito.rest.viewmodels.exception.ExceptionViewModel;
import com.blito.search.SearchViewModel;
import com.blito.services.EventHostService;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestController
@RequestMapping("${api.base.url}" + "/public/event-hosts")
public class PublicEventHostController {
	@Autowired EventHostService eventHostService;
	
	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "get event host by ID")
	@ApiResponses({@ApiResponse(code = 200, message="get event host by ID ok", response = EventHostViewModel.class),
				   @ApiResponse(code = 404, message="NotFoundException", response = ExceptionViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@JsonView(View.EventHost.class)
	@GetMapping("/{eventHostId}")
	public ResponseEntity<EventHostViewModel> getEventHost(@PathVariable long eventHostId)
	{
		return ResponseEntity.ok(eventHostService.get(eventHostId));
	}
	
	@JsonView(View.EventHost.class)
	@GetMapping("/link/{link}")
	public ResponseEntity<EventHostViewModel> getByLink(@PathVariable String link)
	{
		return ResponseEntity.ok(eventHostService.findByEventLink(link));
	}
	
	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "search event hosts")
	@ApiResponses({@ApiResponse(code = 200, message="search ok", response = EventHostViewModel.class),
				   @ApiResponse(code = 404, message="NotFoundException", response = ExceptionViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@JsonView(View.SimpleEventHost.class)
	@GetMapping("/home-page/search")
	public ResponseEntity<Page<EventHostViewModel>> search(Pageable pageable)
	{
		return ResponseEntity.ok(eventHostService.getActiveEventHosts(pageable,7));
	}
}
