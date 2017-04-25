package com.blito.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.blito.enums.OperatorState;
import com.blito.enums.State;
import com.blito.models.ExchangeBlit;

public interface ExchangeBlitRepository extends JpaRepository<ExchangeBlit, Long> {
	List<ExchangeBlit> findByState(State state);

	Page<ExchangeBlit> findByStateAndOperatorState(State state, OperatorState operatorState,Pageable pageable);
}
