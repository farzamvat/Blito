package com.blito.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blito.enums.Response;
import com.blito.exceptions.AlreadyExistsException;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.RoleMapper;
import com.blito.models.Role;
import com.blito.models.User;
import com.blito.repositories.RoleRepository;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.role.RoleViewModel;

@Transactional
@Service
public class RoleService {
	@Autowired RoleRepository roleRepository;
	@Autowired UserRepository userRepository;
	@Autowired UserService userService;
	@Autowired RoleMapper roleMapper;
	
	public Role findRoleById(long id) 
	{
		return Optional.ofNullable(roleRepository.findOne(id))
				.map(r -> r)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.ROLE_NOT_FOUND)));
	}
	
	public RoleViewModel getRoleById(long id) {
		Role role = findRoleById(id);
		return roleMapper.createFromEntity(role);
	}

	public RoleViewModel createRole(RoleViewModel vmodel) {
		Optional<Role> result = roleRepository.findByName(vmodel.getName());
		if (result.isPresent()) {
			throw new AlreadyExistsException(ResourceUtil.getMessage(Response.ROLE_ALREADY_EXISTS));
		}
		return roleMapper.createFromEntity(roleRepository.save(roleMapper.createFromViewModel(vmodel)));
	}

	public RoleViewModel editRole(RoleViewModel vmodel) {
		Role roleById = findRoleById(vmodel.getRoleId());
		return roleRepository.findByName(vmodel.getName()).map(r -> {
			if (r.getRoleId() == roleById.getRoleId()) {
				return roleMapper.createFromEntity(roleRepository.save(roleMapper.updateEntity(vmodel, r)));
			}
			throw new AlreadyExistsException(ResourceUtil.getMessage(Response.ROLE_ALREADY_EXISTS));
		}).orElseGet(() -> roleMapper.createFromEntity(roleRepository.save(roleMapper.createFromViewModel(vmodel))));
	}
	
	public void deleteRoleById(long id)
	{
		Role role = findRoleById(id);
		roleRepository.delete(role);
	}

	public void assignRole(long roleId, long userId) {
		Role roleById = findRoleById(roleId);
		User userById = userService.findUserById(userId);
		userById.getRoles().add(roleById);
		userRepository.save(userById);
	}

	public Page<RoleViewModel> getRoles(Pageable pageable) {
		Page<Role> page = roleRepository.findAll(pageable);
		return roleMapper.toPage(page);
	}

}
