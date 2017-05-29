package com.blito.rest.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blito.rest.viewmodels.blit.BlitViewModel;

@RestController
@RequestMapping("${api.base.url}" + "/blits")
public class BlitController {
	
	@PostMapping
	public ResponseEntity<BlitViewModel> buyBlit(@Validated @RequestBody BlitViewModel vmodel) {
		return null;
	}

}
