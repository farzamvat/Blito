package com.blito.repositories;

import com.blito.models.BlitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Set;

public interface BlitTypeRepository extends JpaRepository<BlitType, Long>  {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	BlitType findByBlitTypeId(long id);
	Set<BlitType> findByBlitTypeIdIn(Set<Long> ids);
}
