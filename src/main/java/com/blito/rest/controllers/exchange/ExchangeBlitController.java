package com.blito.rest.controllers.exchange;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blito.annotations.Permission;
import com.blito.enums.ApiBusinessName;
import com.blito.enums.Response;
import com.blito.mappers.ExchangeBlitMapper;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.View;
import com.blito.rest.viewmodels.exception.ExceptionViewModel;
import com.blito.rest.viewmodels.exchangeblit.ExchangeBlitChangeStateViewModel;
import com.blito.rest.viewmodels.exchangeblit.ExchangeBlitViewModel;
import com.blito.services.ExchangeBlitService;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("${api.base.url}" + "/exchange-blits")
public class ExchangeBlitController {
	
	@Autowired ExchangeBlitService exchangeBlitService;
	@Autowired UserRepository userRepository;
	@Autowired ExchangeBlitMapper exchangeBlitMapper;
	
	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "create exchange blit")
	@ApiResponses({ @ApiResponse(code = 201, message = "created exchange blit", response = ExchangeBlitViewModel.class),
			@ApiResponse(code = 400, message = "ValidationException", response = ExceptionViewModel.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.USER)
	@JsonView(View.ExchangeBlit.class)
	@PostMapping
	public ResponseEntity<ExchangeBlitViewModel> create(@Validated @RequestBody ExchangeBlitViewModel vmodel)
	{
		return ResponseEntity.status(HttpStatus.CREATED).body(exchangeBlitService.create(vmodel));
	}
	
	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "update exchange blit")
	@ApiResponses({ @ApiResponse(code = 202, message = "update exchange blit accepted", response = ExchangeBlitViewModel.class),
			@ApiResponse(code = 400, message = "ValidationException or NotAllowedException", response = ExceptionViewModel.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.USER)
	@JsonView(View.ExchangeBlit.class)
	@PutMapping
	public ResponseEntity<ExchangeBlitViewModel> update(@Validated @RequestBody ExchangeBlitViewModel vmodel)
	{
		return ResponseEntity.accepted().body(exchangeBlitService.update(vmodel));
	}
	
	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "delete exchange blit")
	@ApiResponses({ @ApiResponse(code = 202, message = "delete exchange blit accepted", response = ResultVm.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.USER)
	@DeleteMapping("/{exchangeBlitId}")
	public ResponseEntity<ResultVm> delete(@PathVariable long exchangeBlitId)
	{
		exchangeBlitService.delete(exchangeBlitId);
		return ResponseEntity.accepted().body(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}
	
	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "get all user's exchanbe blits")
	@ApiResponses({ @ApiResponse(code = 200, message = "get all user's exchange blits ok", response = ExchangeBlitViewModel.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.USER)
	@JsonView(View.ExchangeBlit.class)
	@GetMapping("/all")
	public ResponseEntity<Page<ExchangeBlitViewModel>> currentUserExchangeBlits(Pageable pageable)
	{
		return ResponseEntity.ok(exchangeBlitService.currentUserExchangeBlits(pageable));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "change exchnge blit state")
	@ApiResponses({ @ApiResponse(code = 202, message = "change state accepted", response = ResultVm.class),
			@ApiResponse(code = 400, message = "ValidationException or NotAllowedException", response = ExceptionViewModel.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class)})
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.USER)
	@PostMapping("/change-state")
	public ResponseEntity<ResultVm> changeExchangeBlitState(@RequestBody @Validated ExchangeBlitChangeStateViewModel vmodel)
	{
		exchangeBlitService.changeState(vmodel);
		return ResponseEntity.accepted().body(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}
}