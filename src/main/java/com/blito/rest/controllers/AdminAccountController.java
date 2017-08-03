package com.blito.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.blito.annotations.Permission;
import com.blito.enums.ApiBusinessName;

import com.blito.enums.Response;
import com.blito.models.User;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.View;
import com.blito.rest.viewmodels.account.UserViewModel;
import com.blito.rest.viewmodels.exception.ExceptionViewModel;
import com.blito.search.SearchViewModel;
import com.blito.services.AdminAccountService;
import com.blito.services.ExcelService;
import com.blito.services.RoleService;
import com.blito.view.ExcelView;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("${api.base.url}" + "/admin/user-accounts")
public class AdminAccountController {

	@Autowired
	AdminAccountService adminAccountService;
	@Autowired
	ExcelService excelService;
	@Autowired
	RoleService roleService;

	@JsonView(View.AdminUser.class)
	@PostMapping("/search")
	public ResponseEntity<Page<UserViewModel>> search(@RequestBody SearchViewModel<User> search, Pageable pageable) {
		return ResponseEntity.ok(adminAccountService.searchUsers(search, pageable));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "get user")
	@ApiResponses({ @ApiResponse(code = 200, message = "get user ok", response = UserViewModel.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.ADMIN)
	@JsonView(View.AdminUser.class)
	@GetMapping("/{userId}")
	public ResponseEntity<UserViewModel> getUser(@PathVariable long userId) {
		return ResponseEntity.ok(adminAccountService.getUser(userId));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "ban user")
	@ApiResponses({ @ApiResponse(code = 202, message = "ban user accepted", response = ResultVm.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.ADMIN)
	@PutMapping("/ban-user/{userId}")
	public ResponseEntity<ResultVm> banUser(@PathVariable long userId) {
		adminAccountService.banUser(userId);
		return ResponseEntity.accepted().body(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS),true));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "unban user")
	@ApiResponses({ @ApiResponse(code = 202, message = "unban user accepted", response = ResultVm.class),
			@ApiResponse(code = 404, message = "NotFoundException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.ADMIN)
	@PutMapping("/unban-user/{userId}")
	public ResponseEntity<ResultVm> unBanUser(@PathVariable long userId) {
		adminAccountService.unBanUser(userId);
		return ResponseEntity.accepted().body(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS),true));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "assign role to user")
	@ApiResponses({ @ApiResponse(code = 202, message = "role accepted", response = ResultVm.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.ADMIN)
	@GetMapping("/assign/{userId}/{roleId}")
	public ResponseEntity<?> assignRoleToUser(@PathVariable long userId, @PathVariable long roleId) {
		roleService.assignRole(roleId, userId);
		return ResponseEntity.accepted().body(new ResultVm(ResourceUtil.getMessage(Response.SUCCESS),true));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "get users with excel")
	@ApiResponses({ @ApiResponse(code = 200, message = "get all users ok", response = ModelAndView.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.ADMIN)
	@PostMapping("/users-excel.xlsx")
	public ModelAndView searchUsersForExcel(@RequestBody SearchViewModel<User> search) {
		return new ModelAndView(new ExcelView(), adminAccountService.searchUsersForExcel(search));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "update user info")
	@ApiResponses({ @ApiResponse(code = 200, message = "update successful", response = UserViewModel.class),
			@ApiResponse(code = 400, message = "ValidationException", response = ExceptionViewModel.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.ADMIN)
	@JsonView(View.SimpleUser.class)
	@PostMapping("/update-info")
	public ResponseEntity<UserViewModel> updateUserInfo(@Validated @RequestBody UserViewModel vmodel) {
		return ResponseEntity.ok(adminAccountService.updateUserInfo(vmodel));
	}

}
