package com.blito.rest.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blito.rest.viewmodels.EventCreateViewModel;

@RestController
@RequestMapping("${api.base.url}" + "/events")
public class EventController {
	
	@PostMapping("/current-user")
	public ResponseEntity<?> create (EventCreateViewModel vmodel){
		return null;
	}

}
