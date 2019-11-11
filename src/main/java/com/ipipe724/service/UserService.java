package com.ipipe724.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ipipe724.model.Role;
import com.ipipe724.model.User;
import com.ipipe724.repository.RoleRepository;
import com.ipipe724.repository.UserRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service("userService")
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepository.findByRole("ADMIN");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole))); //needs to be updated
        return userRepository.save(user);
    }
    
    public Page<User> getPage(int pageNumber, int amount) {
		Pageable pageable = PageRequest.of(0, 10);
		Page<User> page = userRepository.findAllActive(pageable);
		return page;
	}
    
    public Page<User> getPageAllStatus(int pageNumber, int amount) {
		Pageable pageable = PageRequest.of(0, 10);
		Page<User> page = userRepository.findAll(pageable);
		return page;
	}
    
    
    public User findUser(int id) {
		Optional<User> opt = userRepository.findById(id);
		if (opt != null) {
			return opt.get();
		} else {
			return null;
		}
	}

    public boolean isUserHasRole(String roleName) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.findUserByEmail(auth.getName());
        Set<Role> roles = user.getRoles();
        for(Role role:roles) {
        	if(role.getRole().equals(roleName)) {
        		return true;
        	}
        }
        return false;
    }
    
    public void updateUserSatatus(User user, Integer active) {
    	this.userRepository.updateActiveStatus(active, user.getId());
    }
}