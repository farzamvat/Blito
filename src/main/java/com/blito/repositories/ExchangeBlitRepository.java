package com.blito.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blito.enums.ExchangeBlitState;
import com.blito.models.ExchangeBlit;

public interface ExchangeBlitRepository extends JpaRepository<ExchangeBlit,Long> {
	List<ExchangeBlit> findByState(ExchangeBlitState state);
}
