package com.blito.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.blito.models.EventHost;

public interface EventHostRepository extends JpaRepository<EventHost,Long>, JpaSpecificationExecutor<EventHost>  {
	Optional<EventHost> findByHostName(String hostName);
	List<EventHost> findByUserUserIdAndIsDeletedFalse(long userId);
	Optional<EventHost> findByEventHostIdAndIsDeletedFalse(long eventHostId);
	Page<EventHost> findByIsDeletedFalse(Pageable pageable);
}
