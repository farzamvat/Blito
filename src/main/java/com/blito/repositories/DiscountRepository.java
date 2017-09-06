package com.blito.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blito.models.Discount;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DiscountRepository extends JpaRepository<Discount, Long>, JpaSpecificationExecutor<Discount> {
	Optional<Discount> findByCode(String code);
	Optional<Discount> findByDiscountId(Long discountId);
}
