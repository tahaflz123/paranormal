package com.paranormal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.paranormal.entity.like.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long>{

	boolean existsByUserIdAndPostId(Long id, Long postId);

	void deleteByPostId(Long postId);

	boolean existsByUserIdAndCommentId(Long userId,Long commentId);

	void deleteByCommentId(Long commentId);

	int countByPostId(Long id);

	void removeByCommentId(Long commentId);

	void removeByPostId(Long postId);

}
