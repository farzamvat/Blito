package com.blito.rest.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.blito.models.Image;
import com.blito.repositories.ImageRepository;
import com.blito.rest.viewmodels.ImageViewModel;
import com.blito.services.ImageService;

@RestController
@RequestMapping("${api.base.url}")
public class ImageController {
	
	@Autowired
	ImageService imageService;
	@Autowired
	ImageRepository imageRepository;
	
	@PostMapping("/images/upload")
	public DeferredResult<ResponseEntity<?>> upload(@RequestBody ImageViewModel vmodel)
	{
		DeferredResult<ResponseEntity<?>> deferred = new DeferredResult<>();
		return imageService.save(vmodel.getEncodedBase64())
				.thenApply(uuid -> {
					Image image = new Image();
					image.setImageUUID(uuid);
					image.setImageType(vmodel.getType());
					imageRepository.save(image);
					JSONObject json = new JSONObject();
					json.put("imageId",image.getImageUUID());
					deferred.setResult(ResponseEntity.status(HttpStatus.CREATED).body(json.toString()));
					return deferred;
				})
				.exceptionally(throwable -> {
					deferred.setErrorResult(throwable.getCause());
					return deferred;
				}).join();
	}
	
	@GetMapping("/images/download")
	public DeferredResult<ResponseEntity<String>> download(@RequestParam String id)
	{
		DeferredResult<ResponseEntity<String>> deferred = new DeferredResult<>();
		return imageService.getFileEncodedBase64(id)
				.thenApply(imageEncodedBase64 -> {
					JSONObject json = new JSONObject();
					json.put("imageBase64", imageEncodedBase64);
					deferred.setResult(ResponseEntity.ok(json.toString()));
					return deferred;
				})
				.exceptionally(throwable -> {
					deferred.setErrorResult(throwable.getCause());
					return deferred;
				}).join();
	}
	
}
