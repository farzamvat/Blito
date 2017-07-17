package com.blito.rest.viewmodels.role;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

public class RoleViewModel {
	long roleId;
	@NotNull
	String name;
	Set<Long> permissionIds;
	
	public RoleViewModel()
	{
		permissionIds = new HashSet<>();
	}
	
	public long getRoleId() {
		return roleId;
	}
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<Long> getPermissionIds() {
		return permissionIds;
	}
	public void setPermissionIds(Set<Long> permissionIds) {
		this.permissionIds = permissionIds;
	}
}
