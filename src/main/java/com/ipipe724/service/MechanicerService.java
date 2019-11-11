package com.ipipe724.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ipipe724.model.Mechanicher;

import com.ipipe724.repository.MechanicerRepository;

@Service("mechService")
public class MechanicerService {
	private MechanicerRepository mechRepository;


	@Autowired
	public MechanicerService(MechanicerRepository gradeRepository) {
		this.mechRepository = gradeRepository;
		
	}

	public Page<Mechanicher> getPage(int pageNumber, int amount) {
		Pageable pageable = PageRequest.of(0, 10);
		Page<Mechanicher> page = mechRepository.findAll(pageable);
		return page;
	}

	public Mechanicher findGrade(Long id) {
		Optional<Mechanicher> opt = mechRepository.findById(id);
		if (opt != null) {
			return opt.get();
		} else {
			return null;
		}
	}

	public Mechanicher saveGrade(Mechanicher grade) {
		return mechRepository.save(grade);
	}

	public Iterable<Mechanicher> getAllGrade() {
		
		return mechRepository.findAll();
	}
}
