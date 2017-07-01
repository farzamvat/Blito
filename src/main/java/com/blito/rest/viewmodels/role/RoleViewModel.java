package com.blito.rest.viewmodels.role;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.blito.models.Permission;

public class RoleViewModel {
	long roleId;
	@NotNull
	String name;
	Set<Permission> permissions;
	
	public RoleViewModel()
	{
		permissions = new HashSet<>();
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
	public Set<Permission> getPermissions() {
		return permissions;
	}
	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}
}
