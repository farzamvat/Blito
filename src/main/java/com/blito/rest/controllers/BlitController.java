package com.blito.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.blito.rest.viewmodels.blit.CommonBlitViewModel;
import com.blito.services.BlitService;

@RestController
@RequestMapping("${api.base.url}" + "/blits")
public class BlitController {
	
	@Autowired
	BlitService blitService;
	
	@PostMapping
	public DeferredResult<ResponseEntity<?>> buyBlit(@Validated @RequestBody CommonBlitViewModel vmodel) {
		DeferredResult<ResponseEntity<?>> deferred = new DeferredResult<>();
		return blitService.createCommonBlit(vmodel)
				.thenApply(result -> {
					deferred.setResult(ResponseEntity.status(HttpStatus.CREATED).body(result));
					return deferred;
				}).exceptionally(t -> {
					deferred.setErrorResult(t.getCause().getMessage());
					return deferred;
				}).join();
	}

}
