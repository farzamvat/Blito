package com.blito.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.blito.enums.ApiBusinessName;

@Entity(name="permission")
public class Permission {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	long permissionId;
	
	ApiBusinessName apiBusinessName;
	
	String description;

	public long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(long permissionId) {
		this.permissionId = permissionId;
	}

	public ApiBusinessName getApiBusinessName() {
		return apiBusinessName;
	}

	public void setApiBusinessName(ApiBusinessName apiBusinessName) {
		this.apiBusinessName = apiBusinessName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
