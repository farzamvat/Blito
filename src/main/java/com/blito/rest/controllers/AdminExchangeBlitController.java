package com.blito.rest.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blito.rest.viewmodels.View;
import com.blito.rest.viewmodels.exchangeblit.AdminChangeExchangeBlitStateViewModel;
import com.blito.rest.viewmodels.exchangeblit.ExchangeBlitViewModel;
import com.blito.services.AdminExchangeBlitService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("${api.base.url}" + "/admin/exchange-blits")
public class AdminExchangeBlitController {
	@Autowired AdminExchangeBlitService adminExchangeBlitService;

	@JsonView(View.SimpleExchangeBlit.class)
	@GetMapping
	public ResponseEntity<Page<ExchangeBlitViewModel>> getAll(Pageable pageable) {
		return ResponseEntity.ok(adminExchangeBlitService.exchangeBlitsByPage(pageable));
	}
	
	@PutMapping("/change-state")
	public ResponseEntity<ExchangeBlitViewModel> changeExchangeBlitState(@Validated @RequestBody AdminChangeExchangeBlitStateViewModel vmodel)
	{
		return ResponseEntity.accepted().body(adminExchangeBlitService.changeExchangeBlitState(vmodel));
	}

}
