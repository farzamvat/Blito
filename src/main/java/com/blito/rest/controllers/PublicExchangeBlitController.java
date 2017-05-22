package com.blito.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blito.rest.viewmodels.View;
import com.blito.rest.viewmodels.exchangeblit.ExchangeBlitViewModel;
import com.blito.services.ExchangeBlitService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("${api.base.url}" + "/public/exchange-blits")
public class PublicExchangeBlitController {
	
	@Autowired ExchangeBlitService exchangeBlitService;
	
	@JsonView(View.SimpleExchangeBlit.class)
	@GetMapping("/approved")
	public ResponseEntity<Page<ExchangeBlitViewModel>> approvedExchangeBlits(Pageable pageable)
	{
		return ResponseEntity.ok(exchangeBlitService.getApprovedAndNotClosedOrSoldBlits(pageable));
	}
	
	@JsonView(View.ExchangeBlit.class)
	@GetMapping("/{exchangeBlitId}")
	public ResponseEntity<ExchangeBlitViewModel> getExchangeBlitById(@PathVariable long exchangeBlitId)
	{
		return ResponseEntity.ok(exchangeBlitService.getExchangeBlitById(exchangeBlitId));
	}
}
