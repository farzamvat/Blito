package com.blito.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blito.models.EventHost;

public interface EventHostRepository extends JpaRepository<EventHost,Long> {
	

}
