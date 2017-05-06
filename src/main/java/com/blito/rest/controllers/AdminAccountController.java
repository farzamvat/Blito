package com.blito.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blito.rest.viewmodels.account.UserSimpleViewModel;
import com.blito.rest.viewmodels.account.UserViewModel;
import com.blito.services.AdminService;

@RestController
@RequestMapping("${api.base.url}" + "/admin/user-accounts")
public class AdminAccountController {
	
	@Autowired AdminService adminService;
	
	@GetMapping("/all")
	public ResponseEntity<Page<UserSimpleViewModel>> getAllUsers(Pageable pageable ){
		return ResponseEntity.ok(adminService.getAllUsers(pageable));
	}
	
	@GetMapping
	public ResponseEntity<UserViewModel> getUser(@RequestParam long userId){
		return ResponseEntity.ok(adminService.getUser(userId));
	}
	
	@PutMapping
	public ResponseEntity<UserViewModel> updateUser(){
		return null;
	}
	

}
