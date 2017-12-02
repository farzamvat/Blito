package com.blito.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blito.models.EventDate;

import java.util.Optional;

public interface EventDateRepository extends JpaRepository<EventDate,Long> {
    Optional<EventDate> findByEventDateId(Long eventDateId);
}
