package com.blito.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.blito.models.Event;

public interface EventRepository extends JpaRepository<Event,Long>, JpaSpecificationExecutor<Event> {
	Optional<Event> findByEventLink(String link);
}
