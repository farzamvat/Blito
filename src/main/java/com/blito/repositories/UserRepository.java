package com.blito.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.blito.models.User;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>, PagingAndSortingRepository<User, Long>  {
	Optional<User> findByEmail(String email);
	Optional<User> findByEmailAndBannedFalse(String email);
	Optional<User> findByMobile(String mobile);
	Optional<User> findByEmailAndActivationKey(String email,String key);
	Optional<User> findByRefreshToken(String refreshToken);
	Set<User> findByIsOldUser(boolean isOld);
	Page<User> findAll(Pageable pageable);
	Optional<User> findByUserIdAndBannedFalse(long userId);
	Page<User> findByBannedFalse(Pageable pageable);

}
