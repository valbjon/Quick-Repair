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

import com.ipipe724.model.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    
    @Query(value = "SELECT * FROM user p where p.active = 1", nativeQuery = true)
    Page<User> findAllActive(Pageable pageable);
    
    @Transactional
    @Modifying
    @Query(value = "UPDATE user p set p.active =?1 where p.user_id = ?2", nativeQuery = true)
    void updateActiveStatus(@Param("acive") Integer active, @Param("id") Integer id);
    
    Optional<User> findById(int id);
}