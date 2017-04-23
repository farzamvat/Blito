package com.blito.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blito.models.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {
	List<Role> findByRoleIdIn(List<Long> ids);
	Optional<Role> findByName(String name);
}
