package com.ipipe724.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ipipe724.model.RequestRepair;

import com.ipipe724.repository.RequestRepairRepository;


@Service("requestRepairService")
public class RequestRepairService {
	private RequestRepairRepository gradeRepository;


	@Autowired
	public RequestRepairService(RequestRepairRepository gradeRepository) {
		this.gradeRepository = gradeRepository;
		
	}

	public Page<RequestRepair> getPage(int pageNumber, int amount) {
		Pageable pageable = PageRequest.of(0, 10);
		Page<RequestRepair> page = gradeRepository.findAll(pageable);
		return page;
	}

	public RequestRepair findGrade(Long id) {
		Optional<RequestRepair> opt = gradeRepository.findById(id);
		if (opt != null) {
			return opt.get();
		} else {
			return null;
		}
	}

	public RequestRepair saveGrade(RequestRepair grade) {
		return gradeRepository.save(grade);
	}

	public Iterable<RequestRepair> getAllGrade() {
		
		return gradeRepository.findAll();
	}
	
	public Page<RequestRepair> findAllByUser(int i, int size, int userid) {
		Pageable pageable = PageRequest.of(0, 10);
		Page<RequestRepair> page = gradeRepository.findAllByUser(userid, pageable);
		return page;
	}
	
	
	public Page<RequestRepair> findAllByUserByMechanicker(int i, int size, int userid) {
		Pageable pageable = PageRequest.of(0, 10);
		Page<RequestRepair> page = gradeRepository.findAllByUserByMechanicker(userid, pageable);
		return page;
	}
	
	
	public Page<RequestRepair> findTheLastByID(int i, int size, int userid) {
		Pageable pageable = PageRequest.of(0, 10);
		Page<RequestRepair> page = gradeRepository.findTheLastByID(userid, pageable);
		return page;
	}
	
	
}
