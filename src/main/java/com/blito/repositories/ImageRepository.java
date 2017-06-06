package com.blito.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blito.models.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
	Optional<Image> findByImageUUID(String path);
	List<Image> findByImageUUIDIn(List<String> uuids);
}
