package com.blito.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blito.rest.viewmodels.BannerViewModel;
import com.blito.rest.viewmodels.View;
import com.blito.services.IndexBannerService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("${api.base.url}" + "/admin/index-banner")
public class AdminIndexBannerController {
	
	@Autowired
	IndexBannerService indexBannerService;
	
	@JsonView(View.AdminIndexBanner.class)
	@PostMapping
	public ResponseEntity<?> create(@RequestBody @Validated BannerViewModel vmodel)
	{
		return ResponseEntity.status(HttpStatus.CREATED).body(indexBannerService.create(vmodel));
	}
}
