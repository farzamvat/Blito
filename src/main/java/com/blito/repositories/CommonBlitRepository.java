package com.blito.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.blito.models.CommonBlit;

public interface CommonBlitRepository extends BlitBaseRepository<CommonBlit>, JpaSpecificationExecutor<CommonBlit>, PagingAndSortingRepository<CommonBlit, Long>{
	int countByCustomerEmailAndBlitTypeBlitTypeId(String email,long blitTypeId);
}
