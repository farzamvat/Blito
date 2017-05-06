package com.blito.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.blito.models.User;

public interface UserRepository extends JpaRepository<User,Long> {
	Optional<User> findByEmail(String email);
	Optional<User> findByMobile(String mobile);
	Optional<User> findByActivationKey(String key);
	Optional<User> findByRefreshToken(String refreshToken);
	Page<User> findAll(Pageable pageable);
}
