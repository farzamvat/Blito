package com.blito.rest.controllers;

import java.util.concurrent.CompletionStage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blito.rest.utility.HandleUtility;
import com.blito.rest.viewmodels.blit.CommonBlitViewModel;
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
	
	@PostMapping("/buy-request")
	public CompletionStage<ResponseEntity<?>> buyRequestForFreeBlit(@Validated @RequestBody CommonBlitViewModel vmodel,HttpServletRequest req,HttpServletResponse res)
	{
		return blitService.createCommonBlitUnauthorizedAndNoneFreeBlits(vmodel)
				.handle((result,throwable) -> HandleUtility.generateResponseResult(() -> result, throwable, req, res));
	}
}
