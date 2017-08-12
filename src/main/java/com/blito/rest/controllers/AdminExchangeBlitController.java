package com.blito.rest.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blito.annotations.Permission;
import com.blito.enums.ApiBusinessName;
import com.blito.enums.Response;
import com.blito.models.ExchangeBlit;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.View;
import com.blito.rest.viewmodels.adminreport.BlitBuyerViewModel;
import com.blito.rest.viewmodels.exception.ExceptionViewModel;
import com.blito.rest.viewmodels.exchangeblit.AdminChangeExchangeBlitOperatorStateViewModel;
import com.blito.rest.viewmodels.exchangeblit.AdminChangeExchangeBlitStateViewModel;
import com.blito.rest.viewmodels.exchangeblit.ExchangeBlitViewModel;
import com.blito.search.SearchViewModel;
import com.blito.services.AdminExchangeBlitService;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("${api.base.url}" + "/admin/exchange-blits")
public class AdminExchangeBlitController {
	@Autowired AdminExchangeBlitService adminExchangeBlitService;
	
	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "change exchange blit state")
	@ApiResponses({ @ApiResponse(code = 200, message = "change exchange blit state ok", response = BlitBuyerViewModel.class),
					@ApiResponse(code = 400, message = "ValidationException", response = ExceptionViewModel.class),
					@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.ADMIN)
	@JsonView(View.ExchangeBlit.class)
	@PutMapping("/change-operator-state")
	public ResponseEntity<ExchangeBlitViewModel> changeExchangeBlitOperatorState(@Validated @RequestBody AdminChangeExchangeBlitOperatorStateViewModel vmodel)
	{
		return ResponseEntity.accepted().body(adminExchangeBlitService.changeExchangeBlitOperatorState(vmodel));
	}
	
	@Permission(value = ApiBusinessName.ADMIN)
	@JsonView(View.ExchangeBlit.class)
	@PutMapping("/change-state")
	public ResponseEntity<ExchangeBlitViewModel> changeExchangeBlitState(@Validated @RequestBody AdminChangeExchangeBlitStateViewModel vmodel)
	{
		return ResponseEntity.accepted().body(adminExchangeBlitService.changeExchangeBlitState(vmodel));
	}
	
	@Permission(value = ApiBusinessName.ADMIN)
	@DeleteMapping("/{exchangeBlitId}")
	public ResponseEntity<ResultVm> delete(@PathVariable long exchangeBlitId)
	{
		adminExchangeBlitService.delete(exchangeBlitId);
		return ResponseEntity.accepted().body(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}
	
	@JsonView(View.ExchangeBlit.class)
	@PostMapping("/search")
	@Permission(value = ApiBusinessName.ADMIN)
	public ResponseEntity<Page<ExchangeBlitViewModel>> search(@RequestBody SearchViewModel<ExchangeBlit> searchViewModel,Pageable pageable)
	{
		return ResponseEntity.ok(adminExchangeBlitService.searchExchangeBlits(searchViewModel, pageable));
	}
}
