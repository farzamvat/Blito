package com.blito.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.blito.models.EventHost;

public interface EventHostRepository extends JpaRepository<EventHost,Long>, JpaSpecificationExecutor<EventHost>  {
	Optional<EventHost> findByHostName(String hostName);
	Page<EventHost> findByUserUserIdAndIsDeletedFalse(long userId,Pageable pagable);
	Optional<EventHost> findByEventHostIdAndIsDeletedFalse(long eventHostId);
	Page<EventHost> findByIsDeletedFalse(Pageable pageable);
}
