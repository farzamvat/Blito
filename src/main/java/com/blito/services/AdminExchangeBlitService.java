package com.blito.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blito.enums.Response;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.ExchangeBlitMapper;
import com.blito.models.ExchangeBlit;
import com.blito.repositories.ExchangeBlitRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.exchangeblit.AdminChangeExchangeBlitOperatorStateViewModel;
import com.blito.rest.viewmodels.exchangeblit.AdminChangeExchangeBlitStateViewModel;
import com.blito.rest.viewmodels.exchangeblit.ExchangeBlitViewModel;

@Service
public class AdminExchangeBlitService {
	@Autowired
	ExchangeBlitMapper exchangeBlitMapper;
	@Autowired
	ExchangeBlitRepository exchangeBlitRepository;
	@Autowired
	ImageService imageService;

	
	@Transactional
	public ExchangeBlitViewModel changeExchangeBlitOperatorState (AdminChangeExchangeBlitOperatorStateViewModel vmodel) {
		ExchangeBlit exchangeBlit = exchangeBlitRepository.findByExchangeBlitIdAndIsDeletedFalse(vmodel.getExchangeBlitId())
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_NOT_FOUND)));
		exchangeBlit.setOperatorState(vmodel.getOperatorState().name());
		return exchangeBlitMapper.createFromEntity(exchangeBlit);
	}
	
	@Transactional
	public ExchangeBlitViewModel changeExchangeBlitState(AdminChangeExchangeBlitStateViewModel vmodel)
	{
		ExchangeBlit exchangeBlit = exchangeBlitRepository.findByExchangeBlitIdAndIsDeletedFalse(vmodel.getExchangeBlitId())
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_NOT_FOUND)));
		exchangeBlit.setState(vmodel.getState().name());
		return exchangeBlitMapper.createFromEntity(exchangeBlit);
	}
	
	@Transactional
	public void delete(long exchangeBlitId)
	{
		ExchangeBlit exchangeBlit = exchangeBlitRepository.findByExchangeBlitIdAndIsDeletedFalse(exchangeBlitId)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_NOT_FOUND)));
		imageService.delete(exchangeBlit.getImage().getImageUUID());
		exchangeBlit.setDeleted(true);
		exchangeBlitRepository.save(exchangeBlit);
	}
}
