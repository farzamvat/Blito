package com.blito.rest.controllers;

import java.util.concurrent.CompletionStage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blito.models.CommonBlit;
import com.blito.rest.utility.HandleUtility;
import com.blito.rest.viewmodels.blit.CommonBlitViewModel;
import com.blito.search.SearchViewModel;
import com.blito.services.BlitService;

@RestController
@RequestMapping("${api.base.url}" + "/blits")
public class BlitController {
	
	@Autowired
	BlitService blitService;
	
	@PostMapping("/buy-request")
	public CompletionStage<ResponseEntity<?>> buyBlit(@Validated @RequestBody CommonBlitViewModel vmodel,HttpServletRequest req,HttpServletResponse res) {
		return blitService.createCommonBlit(vmodel).handle((result,throwable) -> HandleUtility.generateResponseResult(() -> result, throwable, req, res));
	}
	
	@PostMapping("/search")
	public ResponseEntity<Page<CommonBlitViewModel>> search(@RequestBody SearchViewModel<CommonBlit> searchViewModel,Pageable pageable)
	{
		return ResponseEntity.ok(blitService.searchCommonBlits(searchViewModel, pageable));
	}

}
