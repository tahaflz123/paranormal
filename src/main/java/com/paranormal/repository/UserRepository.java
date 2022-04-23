package com.paranormal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.paranormal.entity.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	User findByEmail(String email);

	Boolean existsByEmail(String email);

	Boolean existsByEmailOrUsername(String email, String username);

	@Query("SELECT u FROM User u WHERE u.username like lower(concat('%', :q ,'%'))")
	Page<User> findAllByUsernameLike(String q, Pageable pageable);

}
