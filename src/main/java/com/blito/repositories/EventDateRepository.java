package com.blito.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blito.models.EventDate;

public interface EventDateRepository extends JpaRepository<EventDate,Long> {

}
