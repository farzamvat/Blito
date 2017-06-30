package com.blito.mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blito.models.Permission;
import com.blito.models.Role;
import com.blito.repositories.PermissionRepository;
import com.blito.rest.viewmodels.role.RoleViewModel;

@Component
public class RoleMapper implements GenericMapper<Role, RoleViewModel> {

	@Autowired
	PermissionRepository permissionRepository;
	
	private Set<Permission> getPermissionFromRepository(Set<Permission> permissions)
	{
		return permissionRepository.findByPermissionIdIn(
				permissions.stream().map(p -> p.getPermissionId()).collect(Collectors.toSet()));
	}

	@Override
	public Role createFromViewModel(RoleViewModel viewModel) {
		Role role = new Role();
		role.setName(viewModel.getName());
		role.setPermissions(getPermissionFromRepository(viewModel.getPermissions()));
		return role;
	}

	@Override
	public RoleViewModel createFromEntity(Role entity) {
		RoleViewModel vmodel = new RoleViewModel();
		vmodel.setName(entity.getName());
		vmodel.setRoleId(entity.getRoleId());
		vmodel.setPermissions(entity.getPermissions());
		return vmodel;
	}

	@Override
	public Role updateEntity(RoleViewModel viewModel, Role entity) {
		entity.setName(viewModel.getName());
		entity.setPermissions(getPermissionFromRepository(viewModel.getPermissions()));
		return entity;
	}

}
