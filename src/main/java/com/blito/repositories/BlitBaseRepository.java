package com.blito.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.blito.enums.PaymentStatus;
import com.blito.enums.SeatType;
import com.blito.models.Blit;

@NoRepositoryBean
public interface BlitBaseRepository <T extends Blit> extends JpaRepository<T,Long> {
	Optional<T> findByRefNum(String refNum);
	Optional<T> findBySamanTraceNo(String traceNo);
	Optional<T> findByToken(String token);
	Optional<T> findByTrackCode(String trackCode);
	Set<T> findByPaymentStatus(PaymentStatus status);
	Set<T> findBySeatType(SeatType type);
	Optional<T> findByTrackCodeAndPaymentStatus(String trackCode,PaymentStatus status);
	Set<T> findByUserUserId(long userId);
	
}
