package com.paranormal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.paranormal.entity.post.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

	boolean existsByHeader(String header);

	@Query("SELECT p FROM Post p WHERE lower(p.header) like lower(concat('%', :q, '%'))")
	Page<Post> findAllByHeaderLike(String q, Pageable pageable);

	
	@Query("SELECT p FROM Post p WHERE p.user.id = :id")
	List<Post> findAllByUserId(Long id);
	
}
