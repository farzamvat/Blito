package com.blito.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blito.models.Event;

public interface EventRepository extends JpaRepository<Event,Long> {
	Optional<Event> findByEventLink(String link);
}
