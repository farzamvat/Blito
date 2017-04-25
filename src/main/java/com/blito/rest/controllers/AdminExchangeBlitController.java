package com.blito.rest.controllers;

import java.util.Optional;

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

import com.blito.enums.Response;
import com.blito.exceptions.ExchangeBlitNotFoundException;
import com.blito.mappers.ExchangeBlitMapper;
import com.blito.models.ExchangeBlit;
import com.blito.repositories.ExchangeBlitRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.AdminChangeExchangeBlitStateViewModel;
import com.blito.rest.viewmodels.AdminExchangeBlitViewModel;
import com.blito.rest.viewmodels.ResultVm;

@RestController
@RequestMapping("${api.base.url}" + "/admin")
public class AdminExchangeBlitController {
	@Autowired
	ExchangeBlitRepository exchangeBlitRepository;
	@Autowired
	ExchangeBlitMapper exchangeBlitMapper;

	@GetMapping("/exchange-blits")
	public ResponseEntity<Page<AdminExchangeBlitViewModel>> getAll(Pageable pageable) {
		return ResponseEntity.ok(exchangeBlitMapper.toPage(exchangeBlitRepository.findAll(pageable),
				exchangeBlitMapper::exchangeBlitToAdminViewModel));
	}
	
	@PutMapping("/exchange-blits/change-state")
	public ResponseEntity<?> changeExchangeBlitState(@Validated @RequestBody AdminChangeExchangeBlitStateViewModel vmodel)
	{
		ExchangeBlit exchangeBlit = Optional.ofNullable(exchangeBlitRepository.findOne(vmodel.getExchangeBlitId()))
				.map(e -> e)
				.orElseThrow(() -> new ExchangeBlitNotFoundException(ResourceUtil.getMessage(Response.BLIT_NOT_FOUND)));
		exchangeBlit.setOperatorState(vmodel.getOperatorState());
		exchangeBlitRepository.save(exchangeBlit);
		return ResponseEntity.accepted().body(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}
}
