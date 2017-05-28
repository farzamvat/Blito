package com.blito.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blito.models.Discount;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
	Optional<Discount> findByCode(String code);
}
