package com.blito.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blito.enums.ExchangeBlitOperatorState;
import com.blito.enums.ExchangeBlitState;
import com.blito.enums.Response;
import com.blito.exceptions.ExchangeBlitNotFoundException;
import com.blito.exceptions.NotAllowedException;
import com.blito.mappers.ExchangeBlitMapper;
import com.blito.models.ExchangeBlit;
import com.blito.models.User;
import com.blito.repositories.ExchangeBlitRepository;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.ExchangeBlitViewModel;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.security.SecurityContextHolder;

@Service
public class ExchangeBlitService {
	@Autowired ExchangeBlitMapper exchangeBlitMapper;
	@Autowired ExchangeBlitRepository exchangeBlitRepository;
	@Autowired UserRepository userRepository;
	
	public ExchangeBlitViewModel create(ExchangeBlitViewModel vmodel)
	{
		ExchangeBlit exchangeBlit = new ExchangeBlit();
		exchangeBlit = exchangeBlitMapper.ViewModelToExchangeBlit(vmodel, exchangeBlit);
		exchangeBlit.setOperatorState(ExchangeBlitOperatorState.PENDING);
		User user = userRepository.findOne(SecurityContextHolder.currentUser().getUserId());
		exchangeBlit.setUser(user);
		return exchangeBlitMapper.exchangeBlitToViewModel(exchangeBlitRepository.save(exchangeBlit));
	}
	
	public ExchangeBlitViewModel update(ExchangeBlitViewModel vmodel)
	{
		ExchangeBlit exchangeBlit = Optional.ofNullable(exchangeBlitRepository.findOne(vmodel.getExchangeBlitId()))
				.map(e -> e)
				.orElseThrow(() -> new ExchangeBlitNotFoundException(ResourceUtil.getMessage(Response.BLIT_NOT_FOUND)));
		if(exchangeBlit.getOperatorState().equals(ExchangeBlitOperatorState.PENDING)
				|| exchangeBlit.getState().equals(ExchangeBlitState.SOLD)
				|| exchangeBlit.getState().equals(ExchangeBlitState.CLOSED))
		{
			throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
		}
		exchangeBlit = exchangeBlitMapper.ViewModelToExchangeBlit(vmodel, exchangeBlit);
		exchangeBlit.setOperatorState(ExchangeBlitOperatorState.PENDING);
		
		return exchangeBlitMapper.exchangeBlitToViewModel(exchangeBlitRepository.save(exchangeBlit));
	}

	public void delete(long exchangeBlitId) {
		ExchangeBlit exchangeBlit = Optional.ofNullable(exchangeBlitRepository.findOne(exchangeBlitId))
				.map(e -> e)
				.orElseThrow(() -> new ExchangeBlitNotFoundException(ResourceUtil.getMessage(Response.BLIT_NOT_FOUND)));
		exchangeBlitRepository.delete(exchangeBlit);
	}
}
