package com.blito.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blito.models.Blit;

public interface BlitRepository extends JpaRepository<Blit,Long>,BlitBaseRepository<Blit> {
	
}
