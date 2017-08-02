package com.blito.services;

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
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class Initiallizer {
	@Value("${blito.admin.username}")
	String admin_username;
	@Value("${blito.admin.password}")
	String admin_password;
	@Autowired
	PermissionRepository permissionRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	UserAccountService userAccountService;

	@Transactional
	public void importPermissionsToDataBase() {
		List<Permission> allPermission = permissionRepository.findAll();

		if (allPermission.isEmpty()) {
			List<Permission> permissions = new ArrayList<>();
			Arrays.asList(ApiBusinessName.values()).forEach(e -> {
				Permission permission = new Permission();
				permission.setApiBusinessName(e.name());
				permissions.add(permission);
			});
			permissionRepository.save(permissions);
		} else {
			List<String> permStrs = allPermission.stream().map(permission -> permission.getApiBusinessName()).collect(Collectors.toList());

			List<Permission> finalPermissions = Arrays.asList(ApiBusinessName.values())
					.stream()
					.filter(p -> !permStrs.contains(p.name()))
					.map(p1 -> new Permission(p1.name(), ""))
					.collect(Collectors.toList());
			permissionRepository.save(finalPermissions);
		}
	}

	@Transactional
	public void insertAdminUserAndRoleAndOldBlitoUsers() {
		Optional<User> adminResult = userRepository.findByEmail(admin_username);

		Role adminRole = roleRepository.findByName("ADMIN").map(r -> {
			r.setPermissions(permissionRepository.findAll().stream().collect(Collectors.toSet()));
			return roleRepository.save(r);
		}).orElseGet(() -> {
			Role r = new Role();
			r.setName("ADMIN");
			r.setPermissions(permissionRepository.findAll().stream().collect(Collectors.toSet()));
			return roleRepository.save(r);
		});

		Role userRole = roleRepository.findByName("USER").map(r->{
			r.setPermissions(permissionRepository
					.findByApiBusinessNameIn(
							Arrays.asList(ApiBusinessName.values()).stream().filter(p->p.name().equals("USER")).map(p -> p.name()).collect(Collectors.toSet()))
					.stream().collect(Collectors.toSet()));
			return roleRepository.save(r);
		}).orElseGet(() -> {
			Role r = new Role();
			r.setName("USER");
			r.setPermissions(permissionRepository
					.findByApiBusinessNameIn(
							Arrays.asList(ApiBusinessName.values()).stream().filter(p->p.name().equals("USER")).map(p -> p.name()).collect(Collectors.toSet()))
					.stream().collect(Collectors.toSet()));
			return roleRepository.save(r);
		});

		final Role persistedUserRole = roleRepository.save(userRole);

		if (!adminResult.isPresent()) {
			User user = new User();
			user.setEmail(admin_username);
			user.setPassword(encoder.encode(admin_password));
			user.setActive(true);
			user.getRoles().add(adminRole);
			userRepository.save(user);
		}

		// String blitoOldUsers = null;
		// try {
		// blitoOldUsers = new
		// String(Files.readAllBytes(Paths.get("blito_old_users.txt")));
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// List<OldUser> olds = null;
		// try {
		// olds = objectMapper.readValue(blitoOldUsers,
		// TypeFactory.defaultInstance().constructCollectionType(List.class,
		// OldUser.class));
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// List<User> oldUsers = olds.stream().map(u -> {
		// User user = new User();
		// user.setEmail(u.getEmail_address());
		// user.setMobile(u.getCell_number());
		// user.setOldUser(true);
		// user.setActive(true);
		// user.getRoles().add(persistedUserRole);
		// return user;
		// }).collect(Collectors.toList());
		//
		// userRepository.save(oldUsers);

		// olds.forEach(u ->
		// userAccountService.forgetPassword(u.getEmail_address()));
	}

}
