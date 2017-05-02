package com.blito.rest.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blito.enums.Response;
import com.blito.mappers.ExchangeBlitMapper;
import com.blito.models.User;
import com.blito.repositories.ExchangeBlitRepository;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.exchangeblit.ApprovedExchangeBlitViewModel;
import com.blito.rest.viewmodels.exchangeblit.ExchangeBlitViewModel;
import com.blito.rest.viewmodels.exchangeblit.SimpleExchangeBlitViewModel;
import com.blito.rest.viewmodels.exchangeblit.UserEditExchangeBlitViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.ExchangeBlitService;

@RestController
@RequestMapping("${api.base.url}" + "/exchange-blits")
public class ExchangeBlitController {
	
	@Autowired ExchangeBlitService exchangeBlitService;
	@Autowired ExchangeBlitRepository exchangeBlitRepository;
	@Autowired UserRepository userRepository;
	@Autowired ExchangeBlitMapper exchangeBlitMapper;
	
	@PostMapping("/current-user")
	public ResponseEntity<ExchangeBlitViewModel> create(@Validated @RequestBody UserEditExchangeBlitViewModel vmodel)
	{
		return ResponseEntity.status(HttpStatus.CREATED).body(exchangeBlitService.create(vmodel));
	}
	
	@PutMapping("/current-user")
	public ResponseEntity<ExchangeBlitViewModel> update(@Validated @RequestBody UserEditExchangeBlitViewModel vmodel)
	{
		return ResponseEntity.accepted().body(exchangeBlitService.update(vmodel));
	}
	
	@DeleteMapping("/current-user")
	public ResponseEntity<ResultVm> delete(@RequestParam long exchangeBlitId)
	{
		exchangeBlitService.delete(exchangeBlitId);
		return ResponseEntity.accepted().body(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}
	
	@GetMapping("/current-user/all")
	public ResponseEntity<List<SimpleExchangeBlitViewModel>> currentUserExchangeBlits()
	{
		User user = userRepository.findOne(SecurityContextHolder.currentUser().getUserId());
		return ResponseEntity.ok(exchangeBlitMapper.exchangeBlitsToSimpleViewModels(user.getExchangeBlits()));
	}
	
	@GetMapping("/approved")
	public ResponseEntity<Page<ApprovedExchangeBlitViewModel>> approvedExchangeBlits(Pageable pageable)
	{
		return ResponseEntity.ok(exchangeBlitService.getApprovedAndNotClosedOrSoldBlits(pageable));
	}
	
	@GetMapping("/current-user")
	public ResponseEntity<ExchangeBlitViewModel> getExchangeBlitById(@RequestParam long exchangeBlitId)
	{
		return ResponseEntity.ok(exchangeBlitService.getExchangeBlitById(exchangeBlitId));
	}
	
}