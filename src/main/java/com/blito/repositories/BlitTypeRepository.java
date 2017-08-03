package com.blito.repositories;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.blito.models.BlitType;

public interface BlitTypeRepository extends JpaRepository<BlitType, Long>  {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	BlitType findByBlitTypeId(long id);
}
