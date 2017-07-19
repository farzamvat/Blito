package com.blito.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blito.services.BlitService;

@RestController
@RequestMapping("${api.base.url}" + "/public/blits")
public class PublicBlitController {
	
	@Autowired
	private BlitService blitService;
	@GetMapping("/{trackCode}")
	public ResponseEntity<?> getBlit(@PathVariable String trackCode)
	{
		return ResponseEntity.ok(blitService.getBlitByTrackCode(trackCode));
	}
}
