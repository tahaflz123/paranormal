package com.paranormal.service;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paranormal.dto.request.CommentCreationRequest;
import com.paranormal.entity.comment.Comment;
import com.paranormal.entity.post.Post;
import com.paranormal.entity.user.User;
import com.paranormal.exception.AuthenticationException;
import com.paranormal.repository.CommentRepository;

@Service
public class CommentService {
	
	
	private CommentRepository commentRepository;
	private UserService userService;
	private PostService postService;

	@Autowired
	public CommentService(CommentRepository commentRepository, UserService userService, PostService postService) {
		this.commentRepository = commentRepository;
		this.userService = userService;
		this.postService = postService;
	}
	
	public Comment createComment(Long postId, CommentCreationRequest request) {
		Post post = this.postService.findById(postId);
		User user = this.userService.getLoggedInUser();
		if(post == null) {
			throw new AuthenticationException("Post is null");
		}
		
		Comment comment = Comment.builder()
				.content(request.getContent())
				.post(post)
				.sender(user)
				.build();
		comment.setCreatedDate(new Date());
		return this.commentRepository.save(comment);
	}
	
	public List<Comment> findAll(){
		return this.commentRepository.findAll();
	}
	
	

}
