package com.blito.repositories;

import com.blito.models.CommonBlit;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CommonBlitRepository extends BlitBaseRepository<CommonBlit>, JpaSpecificationExecutor<CommonBlit>, PagingAndSortingRepository<CommonBlit, Long>{
	@Query(value = "select SUM(b.count) from common_blit c inner join blit b on c.blit_id = b.blit_id" +
			" where customer_email = ?1 and blit_type_id = ?2",nativeQuery = true)
	int sumCountBlitByEmailAndBlitTypeId(String customerEmail, Long blitTypeId);
}
