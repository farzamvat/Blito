package com.blito.mappers;

import java.util.HashSet;
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
	
	private Set<Permission> getPermissionFromRepository(Set<Long> permissionIds)
	{
		return permissionRepository.findByPermissionIdIn(permissionIds);
				
	}

	@Override
	public Role createFromViewModel(RoleViewModel viewModel) {
		Role role = new Role();
		role.setName(viewModel.getName());
		role.setPermissions(getPermissionFromRepository(viewModel.getPermissionIds()));
		return role;
	}

	@Override
	public RoleViewModel createFromEntity(Role entity) {
		RoleViewModel vmodel = new RoleViewModel();
		vmodel.setName(entity.getName());
		vmodel.setRoleId(entity.getRoleId());
		vmodel.setPermissionIds(entity.getPermissions().stream().map(p->p.getPermissionId()).collect(Collectors.toSet()));
		return vmodel;
	}

	@Override
	public Role updateEntity(RoleViewModel viewModel, Role entity) {
		entity.setName(viewModel.getName());
		entity.setPermissions(getPermissionFromRepository(viewModel.getPermissionIds()));
		return entity;
	}

}
