package com.blito.repositories;

import com.blito.models.Blit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.Set;

@NoRepositoryBean
public interface BlitBaseRepository <T extends Blit> extends JpaRepository<T,Long>, JpaSpecificationExecutor<T>, PagingAndSortingRepository<T, Long> {
	Optional<T> findByToken(String token);
	Optional<T> findByTrackCode(String trackCode);
	Optional<T> findByTrackCodeAndIsDeletedFalse(String trackCode);
	Set<T> findByCustomerEmail(String email);

	// Analytics
	Long countByPaymentStatus(String paymentStatus);

	Long countByPaymentStatusAndCreatedAtGreaterThan(String paymentStatus, Timestamp date);

	@Query(value = "select sum(total_amount) from blit where created_at > ?1 and payment_status = ?2", nativeQuery = true)
	Optional<BigDecimal> sumOfTotalAmountFromDate(Timestamp date, String paymentStatus);

	@Query(value = "select count(distinct customer_email) from blit where user_id is null and payment_status = ?1",nativeQuery = true)
	Optional<BigInteger> countDistinctCustomerEmailByPaymentStatusAndUserIsNull(String paymentStatus);

	@Query(value = "select count(distinct customer_email) from blit where user_id is not null and payment_status = ?1",nativeQuery = true)
	Optional<BigInteger> countDistinctCustomerEmailByPaymentStatusAndUserIsNotNull(String paymentStatus);

	@Query(value = "select count(distinct customer_email) from blit where payment_status = ?1", nativeQuery = true)
	Optional<BigInteger> countDistinctCustomerEmailByPaymentStatus(String paymentStatus);

	@Query(value = "select count(res.customer_email) from \n" +
			"(select customer_email,count(*) from blit where payment_status = \"PAID\" and user_id is not null\n" +
			"group by customer_email having count(*) > 1) as res",nativeQuery = true)
	Optional<BigInteger> numberOfUsersWhoBoughtMoreThanOne();

	@Query(value = "select count(res.customer_email) from \n" +
			"(select customer_email,count(*) from blit where payment_status = \"PAID\" and user_id is null\n" +
			"group by customer_email having count(*) > 1) as res",nativeQuery = true)
	Optional<BigInteger> numberOfCustomersWhoBoughtMoreThanOne();
}
