package com.paranormal.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paranormal.dto.request.CommentCreationRequest;
import com.paranormal.dto.response.CommentResponse;
import com.paranormal.entity.comment.Comment;
import com.paranormal.entity.post.Post;
import com.paranormal.entity.user.User;
import com.paranormal.exception.AuthenticationException;
import com.paranormal.repository.CommentRepository;
import com.paranormal.repository.PostRepository;

@Service
public class CommentService {
	
	
	private CommentRepository commentRepository;
	private UserService userService;
	private PostRepository postRepository;
	
	
	@Autowired
	public CommentService(CommentRepository commentRepository, UserService userService, PostRepository postRepository) {
		this.commentRepository = commentRepository;
		this.userService = userService;
		this.postRepository = postRepository;
	}
	
	public CommentResponse createComment(Long postId, CommentCreationRequest request) {
		Post post = this.postRepository.findById(postId).get();
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
		Comment saved = this.commentRepository.save(comment);
		return CommentResponse.builder().content(saved.getContent())
				.id(comment.getId())
				.sender(UserService.userToResponse(saved.getSender()))
				.postId(postId)
				.build();
	}
	
	public List<Comment> findAll(){
		return this.commentRepository.findAll();
	}

	public List<Comment> findAllByPostId(Long id) {
		return this.commentRepository.findAllByPostId(id);
	}
	
	public static List<CommentResponse> commentsToResponseList(List<Comment> comments){
		List<CommentResponse> response = new ArrayList<CommentResponse>();
		for(Comment comment : comments) {
			response.add(CommentService.commentToResponse(comment));
		}
		return response;
	}
	
	public static CommentResponse commentToResponse(Comment comment) {
		return CommentResponse.builder()
				.id(comment.getId())
				.content(comment.getContent())
				.postId(comment.getPost().getId())
				.sender(UserService.userToResponse(comment.getSender())).build();
	}
}
