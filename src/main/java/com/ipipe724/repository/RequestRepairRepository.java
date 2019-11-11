package com.ipipe724.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ipipe724.model.RequestRepair;


@Repository("RequestRepairRepository")
public interface RequestRepairRepository extends PagingAndSortingRepository<RequestRepair, Long>{

	Optional<RequestRepair> findById(Long id);
	
	Page<RequestRepair> findAll(Pageable pageable);
	
	@Query(value = "SELECT * FROM testrepair p where userid = ?1", nativeQuery = true)
	Page<RequestRepair> findAllByUser(@Param("userid") int userid, Pageable pageable);
	
	@Query(value = "SELECT * FROM testrepair p where id NOT IN (SELECT request_id FROM viewedrequests  where user_id = ?1)", nativeQuery = true)
	Page<RequestRepair> findAllByUserByMechanicker(@Param("user_id") int user_id, Pageable pageable);
	
	
	@Query(value = "SELECT * FROM  testrepair WHERE  id=(SELECT MAX(id) FROM testrepair where userid = ?1)", nativeQuery = true)
	Page<RequestRepair> findTheLastByID(@Param("userid") int userid, Pageable pageable);
	
	
	
}
