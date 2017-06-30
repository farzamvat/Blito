package com.blito.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blito.models.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {
	Set<Role> findByRoleIdIn(List<Long> ids);
	Optional<Role> findByName(String name);
}
