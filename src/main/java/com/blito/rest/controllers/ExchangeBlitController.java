package com.blito.rest.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base.url}" + "/exchange")
public class ExchangeBlitController {
	
	@PostMapping
	public ResponseEntity<?> create(@Validated @RequestBody ExchangeBlitViewModel vmodel)
	{
		
	}
}
