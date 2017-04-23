package com.blito.rest.controllers;


import org.springframework.beans.factory.annotation.Autowired;
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
import com.blito.rest.viewmodels.ExchangeBlitViewModel;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.security.SecurityContextHolder;
import com.blito.services.ExchangeBlitService;

@RestController
@RequestMapping("${api.base.url}" + "/exchange")
public class ExchangeBlitController {
	
	@Autowired ExchangeBlitService exchangeBlitService;
	@Autowired ExchangeBlitRepository exchangeBlitRepository;
	@Autowired UserRepository userRepository;
	@Autowired ExchangeBlitMapper exchangeBlitMapper;
	
	@PostMapping
	public ResponseEntity<ExchangeBlitViewModel> create(@Validated @RequestBody ExchangeBlitViewModel vmodel)
	{
		return ResponseEntity.status(HttpStatus.CREATED).body(exchangeBlitService.create(vmodel));
	}
	
	@PutMapping
	public ResponseEntity<ExchangeBlitViewModel> update(@Validated @RequestBody ExchangeBlitViewModel vmodel)
	{
		return ResponseEntity.accepted().body(exchangeBlitService.update(vmodel));
	}
	
	@DeleteMapping
	public ResponseEntity<ResultVm> delete(@RequestParam long exchangeBlitId)
	{
		exchangeBlitService.delete(exchangeBlitId);
		return ResponseEntity.accepted().body(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}
	
	@GetMapping("/current-user/all")
	public ResponseEntity<?> currentUserBlits()
	{
		User user = userRepository.findOne(SecurityContextHolder.currentUser().getUserId());
		return ResponseEntity.ok(exchangeBlitMapper.exchangeBlitsToViewModels(user.getExchangeBlits()));
	}
}
