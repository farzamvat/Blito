package com.blito.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blito.models.Permission;

public interface PermissionRepository extends JpaRepository<Permission,Long> {
	List<Permission> findByPermissionIdIn(List<Long> ids);
}
