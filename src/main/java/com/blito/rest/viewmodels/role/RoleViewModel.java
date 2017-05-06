package com.blito.rest.viewmodels.role;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.blito.models.Permission;

public class RoleViewModel {
	long roleId;
	@NotNull
	String name;
	List<Permission> permissions;
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
	public List<Permission> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}
}
