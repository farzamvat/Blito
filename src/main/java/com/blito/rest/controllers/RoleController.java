package com.blito.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blito.enums.Response;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.role.RoleViewModel;
import com.blito.services.RoleService;

@RestController
@RequestMapping("${api.base.url}" + "/roles")
public class RoleController {

	@Autowired RoleService roleService;
	
	@PostMapping
	public ResponseEntity<RoleViewModel> create(@Validated @RequestBody RoleViewModel vmodel) {
		return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(vmodel));
	}
	
	@PutMapping
	public ResponseEntity<?> update(@Validated @RequestBody RoleViewModel vmodel) {
		return ResponseEntity.accepted().body(roleService.editRole(vmodel));
	}
	
	@GetMapping("/{roleId}")
	public ResponseEntity<?> get(@PathVariable long roleId) {
		return ResponseEntity.ok(roleService.getRoleById(roleId));
	}
	
	@DeleteMapping("/{roleId}")
	public ResponseEntity<?> delete(@PathVariable long roleId) {
		return ResponseEntity.accepted().body(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}

}
