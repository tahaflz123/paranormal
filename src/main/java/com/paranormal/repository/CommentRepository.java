package com.paranormal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paranormal.entity.comment.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{

}
