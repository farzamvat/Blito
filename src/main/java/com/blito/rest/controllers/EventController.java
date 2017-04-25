package com.blito.rest.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blito.rest.viewmodels.CreateEventViewModel;

@RestController
@RequestMapping("${api.base.url}" + "/events")
public class EventController {
	
	@PostMapping("/current-user")
	public ResponseEntity<?> create (CreateEventViewModel vmodel){
		return null;
	}

}
