package com.blito.security;

import java.util.List;
import java.util.stream.Collectors;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blito.annotations.Permission;
import com.blito.enums.Response;
import com.blito.exceptions.ForbiddenException;
import com.blito.models.Role;
import com.blito.models.User;
import com.blito.repositories.RoleRepository;
import com.blito.resourceUtil.ResourceUtil;

@Service
@Aspect
public class RoleAspect {

	@Autowired
	RoleRepository roleRepository;

	@Before("@annotation(permission)")
	public void filterByRole(JoinPoint joinPoint, Permission permission) {
		User currentUser = SecurityContextHolder.currentUser();

		List<Role> currentUserAttachedRoles = roleRepository
				.findByRoleIdIn(currentUser.getRoles().stream().map(r -> r.getRoleId()).collect(Collectors.toList()));

		if (currentUserAttachedRoles != null) {
			if (currentUserAttachedRoles.stream().flatMap(r -> r.getPermissions().stream()).distinct()
					.noneMatch(p -> p.getApiBusinessName().equals(permission.value()))) {
				throw new ForbiddenException(ResourceUtil.getMessage(Response.ACCESS_DENIED));
			}
		} else {
			throw new ForbiddenException(ResourceUtil.getMessage(Response.ACCESS_DENIED));
		}
	}
}