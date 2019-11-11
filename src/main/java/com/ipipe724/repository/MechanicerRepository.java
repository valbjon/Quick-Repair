package com.ipipe724.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


import com.ipipe724.model.Mechanicher;


@Repository("mechRepository")
public interface MechanicerRepository extends PagingAndSortingRepository<Mechanicher, Long>{

	Optional<Mechanicher> findById(Long id);
	
	Page<Mechanicher> findAll(Pageable pageable);
	
}
