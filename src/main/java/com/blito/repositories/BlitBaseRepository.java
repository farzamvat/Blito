package com.blito.repositories;

import com.blito.enums.PaymentStatus;
import com.blito.enums.SeatType;
import com.blito.models.Blit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.Set;

@NoRepositoryBean
public interface BlitBaseRepository <T extends Blit> extends JpaRepository<T,Long>, JpaSpecificationExecutor<T>, PagingAndSortingRepository<T, Long> {
	Optional<T> findByRefNum(String refNum);
	Optional<T> findBySamanTraceNo(String traceNo);
	Optional<T> findByToken(String token);
	Optional<T> findByTrackCode(String trackCode);
	Set<T> findByPaymentStatus(PaymentStatus status);
	Set<T> findBySeatType(SeatType type);
	Optional<T> findByTrackCodeAndPaymentStatus(String trackCode,PaymentStatus status);
	Set<T> findByUserUserId(long userId);
	Set<T> findByCustomerEmail(String email);
}
