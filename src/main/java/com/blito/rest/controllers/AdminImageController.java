package com.blito.rest.controllers;

import java.util.concurrent.CompletionStage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blito.rest.utility.HandleUtility;
import com.blito.rest.viewmodels.image.ImageViewModel;
import com.blito.services.ImageService;

@RestController
@RequestMapping("${api.base.url}" + "/admin/images")
public class AdminImageController {

	@Autowired
	private ImageService imageService;
	
	@PostMapping
	public CompletionStage<ResponseEntity<?>> uploadDefaultImages(@RequestParam("file") MultipartFile file,@RequestParam("defaultId") String defaultId, HttpServletRequest req,HttpServletResponse res)
	{
		return imageService.createOrUpdateDefaultImage(file, defaultId)
				.handle((result,throwable) -> HandleUtility.generateResponseResult(() -> result, throwable, req, res));
	}
	
	@GetMapping
	public ResponseEntity<Page<ImageViewModel>> getDeafaultImages(){
		return ResponseEntity.accepted().body(imageService.getDefaultImages());
	}
}
