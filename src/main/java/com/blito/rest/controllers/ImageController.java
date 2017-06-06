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

import com.blito.enums.Response;
import com.blito.exceptions.NotFoundException;
import com.blito.models.Image;
import com.blito.repositories.ImageRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.View;
import com.blito.rest.viewmodels.View;
import com.blito.rest.viewmodels.image.ImageBase64ViewModel;
import com.blito.rest.viewmodels.image.ImageViewModel;

import com.fasterxml.jackson.annotation.JsonView;import com.blito.services.ImageService;

@RestController
@RequestMapping("${api.base.url}")
public class ImageController {
	
	@Autowired
	ImageService imageService;
	@Autowired
	ImageRepository imageRepository;
	
	@JsonView(View.DefaultView.class)
	@PostMapping("/images/upload")
	public DeferredResult<ResponseEntity<ImageViewModel>> upload(@RequestBody ImageBase64ViewModel vmodel)
	{
		DeferredResult<ResponseEntity<ImageViewModel>> deferred = new DeferredResult<>();
		return imageService.save(vmodel.getEncodedBase64())
				.thenApply(uuid -> {
					Image image = new Image();
					image.setImageUUID(uuid);
					imageRepository.save(image);
					ImageViewModel responseVmodel = new ImageViewModel();
					responseVmodel.setImageUUID(image.getImageUUID());
					deferred.setResult(ResponseEntity.status(HttpStatus.CREATED).body(responseVmodel));
					return deferred;
				})
				.exceptionally(throwable -> {
					deferred.setErrorResult(throwable.getCause());
					return deferred;
				}).join();
	}
	
	@JsonView(View.DefaultView.class)
	@GetMapping("/images/download")
	public DeferredResult<ResponseEntity<ImageBase64ViewModel>> download(@RequestParam String id)
	{
		DeferredResult<ResponseEntity<ImageBase64ViewModel>> deferred = new DeferredResult<>();
		Image image = imageRepository.findByImageUUID(id)
			.map(i -> i)
			.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND)));
		
		return imageService.getFileEncodedBase64(image.getImageUUID())
				.thenApply(imageEncodedBase64 -> {
					ImageBase64ViewModel responseViewModel = new ImageBase64ViewModel();
					responseViewModel.setEncodedBase64(imageEncodedBase64);
					deferred.setResult(ResponseEntity.ok(responseViewModel));
					return deferred;
				})
				.exceptionally(throwable -> {
					deferred.setErrorResult(throwable.getCause());
					return deferred;
				}).join();
	}
	
}
