package com.ipipe724.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ipipe724.model.Role;

import com.ipipe724.repository.RoleRepository;


@Service("roleService")
public class RoleService {

	private RoleRepository roleRepository;


	@Autowired
	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
		
	}

	
	public Iterable<Role> getAllRole() {
		
		return roleRepository.findAll();
	}
	
	
}
