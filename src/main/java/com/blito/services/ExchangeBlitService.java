package com.blito.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.blito.enums.OperatorState;
import com.blito.enums.Response;
import com.blito.enums.State;
import com.blito.exceptions.NotAllowedException;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.ExchangeBlitMapper;
import com.blito.models.ExchangeBlit;
import com.blito.models.User;
import com.blito.repositories.ExchangeBlitRepository;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.exchangeblit.ApprovedExchangeBlitViewModel;
import com.blito.rest.viewmodels.exchangeblit.ExchangeBlitViewModel;
import com.blito.rest.viewmodels.exchangeblit.UserEditExchangeBlitViewModel;
import com.blito.security.SecurityContextHolder;

@Service
public class ExchangeBlitService {
	@Autowired
	ExchangeBlitMapper exchangeBlitMapper;
	@Autowired
	ExchangeBlitRepository exchangeBlitRepository;
	@Autowired
	UserRepository userRepository;
	
	private ExchangeBlit findByExchangeBlitId(long id) 
	{
		return Optional.ofNullable(exchangeBlitRepository.findOne(id))
				.map(e -> e)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_NOT_FOUND)));
	}

	public ExchangeBlitViewModel create(UserEditExchangeBlitViewModel vmodel) {
		ExchangeBlit exchangeBlit = new ExchangeBlit();
		exchangeBlit = exchangeBlitMapper.userEditViewModelToExchangeBlit(vmodel, exchangeBlit);
		exchangeBlit.setState(State.OPEN);
		exchangeBlit.setOperatorState(OperatorState.PENDING);
		User user = userRepository.findOne(SecurityContextHolder.currentUser().getUserId());
		exchangeBlit.setUser(user);
		return exchangeBlitMapper.createFromEntity(exchangeBlitRepository.save(exchangeBlit));
	}

	public ExchangeBlitViewModel update(UserEditExchangeBlitViewModel vmodel) {
		ExchangeBlit exchangeBlit = findByExchangeBlitId(vmodel.getExchangeBlitId());
		if (exchangeBlit.getOperatorState().equals(OperatorState.PENDING)
				|| exchangeBlit.getState().equals(State.SOLD) || exchangeBlit.getState().equals(State.CLOSED)
				|| exchangeBlit.getUser().getUserId() != SecurityContextHolder.currentUser().getUserId()) {
			throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
		}
		exchangeBlit = exchangeBlitMapper.userEditViewModelToExchangeBlit(vmodel, exchangeBlit);
		exchangeBlit.setOperatorState(OperatorState.PENDING);

		return exchangeBlitMapper.createFromEntity(exchangeBlitRepository.save(exchangeBlit));
	}

	public void delete(long exchangeBlitId) {
		ExchangeBlit exchangeBlit = findByExchangeBlitId(exchangeBlitId);
		exchangeBlitRepository.delete(exchangeBlit);
	}

	public Page<ApprovedExchangeBlitViewModel> getApprovedAndNotClosedOrSoldBlits(Pageable pageable) {
		return exchangeBlitMapper.toPage(exchangeBlitRepository.findByStateAndOperatorState(State.OPEN,
				OperatorState.APPROVED, pageable), exchangeBlitMapper::exchangeBlitToApprovedViewModel);

	}
	
	public ExchangeBlitViewModel getExchangeBlitById(long exchangeBlitId)
	{
		return exchangeBlitMapper.createFromEntity(findByExchangeBlitId(exchangeBlitId));
	}
}
