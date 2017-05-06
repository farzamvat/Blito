package com.blito.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blito.enums.Response;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.account.UserAdminUpdateViewModel;
import com.blito.rest.viewmodels.account.UserSimpleViewModel;
import com.blito.rest.viewmodels.account.UserViewModel;
import com.blito.services.AdminService;
import com.blito.services.RoleService;

@RestController
@RequestMapping("${api.base.url}" + "/admin/user-accounts")
public class AdminAccountController {
	
	@Autowired AdminService adminService;
	@Autowired RoleService roleService;
	
	@GetMapping("/all")
	public ResponseEntity<Page<UserSimpleViewModel>> getAllUsers(Pageable pageable ){
		return ResponseEntity.ok(adminService.getAllUsers(pageable));
	}
	
	@GetMapping
	public ResponseEntity<UserViewModel> getUser(@RequestParam long userId){
		return ResponseEntity.ok(adminService.getUser(userId));
	}
	
	@PutMapping
	public ResponseEntity<UserViewModel> updateUser(UserAdminUpdateViewModel vmodel){
		return ResponseEntity.ok(adminService.updateUser(vmodel));
	}
	
	@GetMapping("/assign/{userId}/{roleId}")
	public ResponseEntity<?> assignRoleToUser(@PathVariable long userId,@PathVariable long roleId)
	{
		roleService.assignRole(roleId, userId);
		return ResponseEntity.accepted().body(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)));
	}
	
}
