package com.blito.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blito.models.Event;

public interface EventRepository extends JpaRepository<Event,Long> {

}
