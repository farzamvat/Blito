package com.blito.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blito.models.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
	Optional<Image> findByImageUUID(String path);
}
