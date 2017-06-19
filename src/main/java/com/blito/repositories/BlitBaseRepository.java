package com.blito.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.blito.enums.BlitTypeEnum;
import com.blito.enums.PaymentStatus;
import com.blito.models.Blit;

@NoRepositoryBean
public interface BlitBaseRepository <T extends Blit> extends JpaRepository<T,Long> {
	Optional<T> findBySamanBankRefNumber(String refNum);
	Optional<T> findBySamanTraceNo(String traceNo);
	Optional<T> findBySamanBankToken(String token);
	Optional<T> findByTrackCode(String trackCode);
	List<T> findByPaymentStatus(PaymentStatus status);
	List<T> findByType(BlitTypeEnum type);
	Optional<T> findByTrackCodeAndPaymentStatus(String trackCode,PaymentStatus status);
	List<T> findByUserUserId(long userId);
	
}
