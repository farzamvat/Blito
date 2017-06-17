package com.blito.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.blito.models.EventHost;

public interface EventHostRepository extends JpaRepository<EventHost,Long>, JpaSpecificationExecutor<EventHost>  {
	Optional<EventHost> findByHostName(String hostName);
}
