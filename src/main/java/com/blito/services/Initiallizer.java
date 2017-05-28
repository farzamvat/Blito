package com.blito.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blito.enums.ApiBusinessName;
import com.blito.models.Permission;
import com.blito.models.Role;
import com.blito.models.User;
import com.blito.repositories.PermissionRepository;
import com.blito.repositories.RoleRepository;
import com.blito.repositories.UserRepository;
import com.blito.rest.viewmodels.OldUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

@Service
public class Initiallizer {
	@Value("${blito.admin.username}")
	String admin_username;
	@Value("${blito.admin.password}")
	String admin_password;
	@Autowired PermissionRepository permissionRepository;
	@Autowired RoleRepository roleRepository;
	@Autowired UserRepository userRepository;
	@Autowired PasswordEncoder encoder;
	@Autowired ObjectMapper objectMapper;
	@Autowired UserAccountService userAccountService;

	@Transactional
	public void importPermissionsToDataBase() {
		if (permissionRepository.findAll().isEmpty()) {
			List<Permission> permissions = new ArrayList<>();
			Arrays.asList(ApiBusinessName.values()).forEach(e -> {
				Permission permission = new Permission();
				permission.setApiBusinessName(e);
				permissions.add(permission);
			});
			permissionRepository.save(permissions);
		} else {
			List<Permission> finalPermissions = Arrays.asList(ApiBusinessName.values()).stream().map(e -> permissionRepository.findAll()
					.stream().filter(p -> p.getApiBusinessName().equals(e)).findFirst().map(p -> p).orElseGet(() -> {
						Permission permission = new Permission();
						permission.setApiBusinessName(e);
						return permission;
					})).collect(Collectors.toList());
			permissionRepository.save(finalPermissions);
		}
	}
	
	@Transactional
	public void insertAdminUserAndRoleAndOldBlitoUsers()
	{
		Optional<User> adminResult = userRepository.findByEmail(admin_username);
			
		Role adminRole = roleRepository.findByName("ADMIN").map(r -> {
			r.setPermissions(permissionRepository.findAll());
			return roleRepository.save(r);
		})
		.orElseGet(() -> {
			Role r = new Role();
			r.setName("ADMIN");	r.setPermissions(permissionRepository.findAll());
			return roleRepository.save(r);
		});
		
		Role userRole = roleRepository.findByName("USER")
				.map(r -> {
					r.setPermissions(permissionRepository.findAll());
					return r;
				})
				.orElseGet(() -> {
					Role r = new Role();
					r.setName("USER"); r.setPermissions(permissionRepository.findAll());
					return r;
				});
		
		final Role persistedUserRole = roleRepository.save(userRole);
		String blitoOldUsers = null;
		try {
			blitoOldUsers = new String(Files.readAllBytes(Paths.get("blito_old_users.txt")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<OldUser> olds = null;
		try {
			olds = objectMapper.readValue(blitoOldUsers, TypeFactory.defaultInstance().constructCollectionType(List.class, OldUser.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<User> oldUsers = olds.stream().map(u -> {
			User user = new User();
			user.setEmail(u.getEmail_address());
			user.setMobile(u.getCell_number());
			user.setOldUser(true);
			user.setActive(true);
			user.getRoles().add(persistedUserRole);
			return user;
		}).collect(Collectors.toList());
		
		userRepository.save(oldUsers);
		
		if(!adminResult.isPresent()) {
			User user = new User();
			user.setEmail(admin_username);
			user.setPassword(encoder.encode(admin_password));
			user.setActive(true);
			user.getRoles().add(adminRole);
			userRepository.save(user);
		}
		
		olds.forEach(u -> userAccountService.forgetPassword(u.getEmail_address()));
	}
	
}
