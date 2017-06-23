package com.blito.rest.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blito.models.ExchangeBlit;
import com.blito.rest.viewmodels.View;
import com.blito.rest.viewmodels.exception.ExceptionViewModel;
import com.blito.rest.viewmodels.exchangeblit.ExchangeBlitViewModel;
import com.blito.search.SearchViewModel;
import com.blito.services.ExchangeBlitService;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("${api.base.url}" + "/public/exchange-blits")
public class PublicExchangeBlitController {
	
	@Autowired ExchangeBlitService exchangeBlitService;
	
	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "get approved exchange blits")
	@ApiResponses({ @ApiResponse(code = 200, message = "get all approved exchange blits ok", response = ExchangeBlitViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@JsonView(View.SimpleExchangeBlit.class)
	@GetMapping("/approved")
	public ResponseEntity<Page<ExchangeBlitViewModel>> approvedExchangeBlits(Pageable pageable)
	{
		return ResponseEntity.ok(exchangeBlitService.getApprovedAndNotClosedOrSoldBlits(pageable));
	}
	
	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "get exchange blit")
	@ApiResponses({ @ApiResponse(code = 200, message = "get exchange blit ok", response = ExchangeBlitViewModel.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@JsonView(View.ExchangeBlit.class)
	@GetMapping("/{exchangeBlitId}")
	public ResponseEntity<ExchangeBlitViewModel> getExchangeBlitById(@PathVariable long exchangeBlitId)
	{
		return ResponseEntity.ok(exchangeBlitService.getExchangeBlitById(exchangeBlitId));
	}
	
	@JsonView(View.ExchangeBlit.class)
	@PostMapping("/search")
	public ResponseEntity<Page<ExchangeBlitViewModel>> search(@RequestBody SearchViewModel<ExchangeBlit> searchViewModel,Pageable pageable)
	{
		return ResponseEntity.ok(exchangeBlitService.searchExchangeBlits(searchViewModel, pageable));
	}
}
