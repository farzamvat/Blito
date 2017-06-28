package com.blito.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.blito.enums.OperatorState;
import com.blito.enums.State;
import com.blito.models.ExchangeBlit;

public interface ExchangeBlitRepository extends JpaRepository<ExchangeBlit, Long>, JpaSpecificationExecutor<ExchangeBlit>, PagingAndSortingRepository<ExchangeBlit, Long> {
	List<ExchangeBlit> findByStateAndIsDeletedFalse(State state);

	Page<ExchangeBlit> findByStateAndOperatorStateAndIsDeletedFalse(State state, OperatorState operatorState,Pageable pageable);
	
	Optional<ExchangeBlit> findByExchangeBlitIdAndIsDeletedFalse(long exchangeBlitId);
	
	Page<ExchangeBlit> findByUserUserIdAndIsDeletedFalse(long userId,Pageable pageable);
}
