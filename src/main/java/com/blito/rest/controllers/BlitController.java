package com.blito.rest.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blito.rest.viewmodels.blit.CommonBlitViewModel;

@RestController
@RequestMapping("${api.base.url}" + "/blits")
public class BlitController {
	
	@PostMapping
	public ResponseEntity<CommonBlitViewModel> buyBlit(@Validated @RequestBody CommonBlitViewModel vmodel) {
		return null;
	}

}
