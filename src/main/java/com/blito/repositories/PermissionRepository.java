package com.blito.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blito.models.Permission;

public interface PermissionRepository extends JpaRepository<Permission,Long> {
	Set<Permission> findByPermissionIdIn(Set<Long> ids);
}
