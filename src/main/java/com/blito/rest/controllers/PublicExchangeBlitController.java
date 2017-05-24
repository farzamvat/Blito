package com.blito.rest.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.blito.exceptions.ExceptionUtil;
import com.blito.exceptions.NotFoundException;
import com.blito.rest.viewmodels.View;
import com.blito.rest.viewmodels.exception.ExceptionViewModel;
import com.blito.rest.viewmodels.exchangeblit.ExchangeBlitViewModel;
import com.blito.services.ExchangeBlitService;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("${api.base.url}" + "/public/exchange-blits")
public class PublicExchangeBlitController {
	
	@Autowired ExchangeBlitService exchangeBlitService;
	
//	@ResponseStatus(HttpStatus.NOT_FOUND)
//	@ExceptionHandler({ NotFoundException.class })
//	public ExceptionViewModel notFound(HttpServletRequest request, RuntimeException exception) {
//		return ExceptionUtil.generate(HttpStatus.NOT_FOUND, request, exception);
//	}
	
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
}
