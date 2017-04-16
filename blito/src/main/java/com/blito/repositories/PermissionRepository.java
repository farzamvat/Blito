package com.blito.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blito.models.Permission;

public interface PermissionRepository extends JpaRepository<Permission,Long> {

}
