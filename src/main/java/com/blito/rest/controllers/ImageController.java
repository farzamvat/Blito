package com.blito.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blito.services.ImageService;

@RestController
@RequestMapping("${api.base.url}")
public class ImageController {
	
	@Autowired
	ImageService imageService;
	
	@PostMapping("/images/upload")
	public ResponseEntity<?> upload(@RequestBody String encodedBase64)
	{
		String uuid = imageService.save(encodedBase64);
		return ResponseEntity.ok(uuid);
	}
	
}
