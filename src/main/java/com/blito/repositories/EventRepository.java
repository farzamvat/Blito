package com.blito.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.blito.models.Event;

public interface EventRepository
		extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event>, PagingAndSortingRepository<Event, Long> {
	Optional<Event> findByEventLink(String link);
}
