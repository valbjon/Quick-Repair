package com.ipipe724.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ipipe724.model.Mechanicher;
import com.ipipe724.model.RequestRepair;
import com.ipipe724.model.Role;
import com.ipipe724.model.User;
import com.ipipe724.model.ViewedRequests;
import com.ipipe724.repository.RoleRepository;
import com.ipipe724.repository.UserRepository;
import com.ipipe724.repository.ViewedRequestsRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service("viewedRequestsService")
public class ViewedRequestsService {
	private ViewedRequestsRepository viewedRequestRepository;

	@Autowired
	public ViewedRequestsService(ViewedRequestsRepository roleRepository) {
		this.viewedRequestRepository = roleRepository;
		
	}
	
	public ViewedRequests saveGrade(ViewedRequests grade) {
		return viewedRequestRepository.save(grade);
	}
	
	public ViewedRequests findGrade(int id) {
		Optional<ViewedRequests> opt = viewedRequestRepository.findById(id);
		if (opt != null) {
			return opt.get();
		} else {
			return null;
		}
	}
	
	public Page<ViewedRequests> findAllAcceptedByMechanicker(int i, int size, int userid) {
		Pageable pageable = PageRequest.of(0, 1000);
		Page<ViewedRequests> page = viewedRequestRepository.findAllAcceptedByMechanicker(userid, pageable);
		return page;
	}
	
	public Page<ViewedRequests> findAllNotificationsForUser(int i, int size, int userid) {
		Pageable pageable = PageRequest.of(0, 1000);
		Page<ViewedRequests> page = viewedRequestRepository.findAllNotificationsForUser(userid, pageable);
		return page;
	}
	
	public Page<ViewedRequests> findAllForDeleteStatusUpdate(int i, int size, int userid, int id) {
		Pageable pageable = PageRequest.of(0, 1000);
		Page<ViewedRequests> page = viewedRequestRepository.findAllForDeleteStatusUpdate(userid, id, pageable);
		return page;
	}
}
