package com.blito.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.blito.enums.OperatorState;
import com.blito.enums.State;
import com.blito.models.Event;
import com.blito.models.ExchangeBlit;

public interface ExchangeBlitRepository extends JpaRepository<ExchangeBlit, Long>, JpaSpecificationExecutor<ExchangeBlit>, PagingAndSortingRepository<ExchangeBlit, Long> {
	List<ExchangeBlit> findByState(State state);

	Page<ExchangeBlit> findByStateAndOperatorState(State state, OperatorState operatorState,Pageable pageable);
}
