package com.ipipe724.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ipipe724.model.RequestRepair;
import com.ipipe724.model.User;
import com.ipipe724.model.ViewedRequests;

@Repository("viewedRequestsRepository")
public interface ViewedRequestsRepository extends JpaRepository<ViewedRequests, Long> {
    
    Optional<ViewedRequests> findById(int id);
    
    @Query(value = "SELECT * FROM viewedrequests p where p.status = 'accept' and p.user_id = ?1", nativeQuery = true)
	Page<ViewedRequests> findAllAcceptedByMechanicker(@Param("user_id") int user_id, Pageable pageable);
    
    @Query(value = "SELECT * FROM viewedrequests WHERE request_id IN (SELECT id FROM testrepair WHERE userid = ?1) and status = 'accept' ORDER BY id desc", nativeQuery = true)
	Page<ViewedRequests> findAllNotificationsForUser(@Param("userid") int userid, Pageable pageable);
    
    @Query(value = "SELECT * FROM viewedrequests WHERE request_id = ?1 and id != ?2", nativeQuery = true)
	Page<ViewedRequests> findAllForDeleteStatusUpdate(@Param("request_id") int userid, @Param("id") int id, Pageable pageable);
}