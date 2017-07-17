package com.blito.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.blito.enums.OperatorState;
import com.blito.enums.State;
import com.blito.models.ExchangeBlit;

public interface ExchangeBlitRepository extends JpaRepository<ExchangeBlit, Long>, JpaSpecificationExecutor<ExchangeBlit>, PagingAndSortingRepository<ExchangeBlit, Long> {
	Set<ExchangeBlit> findByStateAndIsDeletedFalse(State state);

	Page<ExchangeBlit> findByStateAndOperatorStateAndIsDeletedFalse(String state, String operatorState,Pageable pageable);
	
	Optional<ExchangeBlit> findByExchangeBlitIdAndIsDeletedFalse(long exchangeBlitId);
	
	Page<ExchangeBlit> findByUserUserIdAndIsDeletedFalse(long userId,Pageable pageable);
	
	Optional<ExchangeBlit> findByExchangeLinkAndIsDeletedFalse(String exchangeLink);
}
