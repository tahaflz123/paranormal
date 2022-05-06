package com.paranormal.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paranormal.dto.request.CommentCreationRequest;
import com.paranormal.dto.response.CommentResponse;
import com.paranormal.entity.comment.Comment;
import com.paranormal.entity.post.Post;
import com.paranormal.entity.user.User;
import com.paranormal.entity.user.UserRole;
import com.paranormal.exception.ErrorCode;
import com.paranormal.exception.ParanormalException;
import com.paranormal.repository.CommentRepository;
import com.paranormal.repository.PostRepository;
import com.paranormal.service.ErrorMessagesService.Key;


@Service
public class CommentService {
	
	
	private CommentRepository commentRepository;
	private UserService userService;
	private PostRepository postRepository;
	private ErrorMessagesService errorMessagesService;
	
	private final Logger logger = LoggerFactory.getLogger(CommentService.class);
	
	
	@Autowired
	public CommentService(CommentRepository commentRepository, UserService userService,
			PostRepository postRepository, ErrorMessagesService errorMessagesService) {
		this.commentRepository = commentRepository;
		this.userService = userService;
		this.postRepository = postRepository;
		this.errorMessagesService = errorMessagesService;
	}
	
	public CommentResponse createComment(Long postId, CommentCreationRequest request) {
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new ParanormalException(ErrorCode.NO_RESOURCE, ErrorMessagesService.getMessage(Key.NO_RESOURCE)));
		User user = this.userService.getLoggedInUser();
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
		if(comments != null) {
			for(Comment comment : comments) {
					response.add(CommentService.commentToResponse(comment));
			}
		}
		return response;
	}
	
	public static CommentResponse commentToResponse(Comment comment) {
		if(comment == null) {
			return null;
		}
		return CommentResponse.builder()
				.id(comment.getId())
				.content(comment.getContent())
				.postId(comment.getPost().getId())
				.sender(UserService.userToResponse(comment.getSender())).build();
	}
	
	public CommentResponse findByIdCommentResponse(Long commentId) {
		return CommentService.commentToResponse(findById(commentId));
	}

	public Comment findById(Long commentId) {
		return this.commentRepository.findById(commentId).orElseThrow(() -> new ParanormalException(ErrorCode.NO_RESOURCE,
				ErrorMessagesService.getMessage(Key.NO_RESOURCE)));
	}

	public Boolean deleteById(Long id) {
		Comment comment = this.findById(id);
		User user = this.userService.getLoggedInUser();

		if(comment.getDeleted()) {
			throw new ParanormalException(ErrorCode.ALREADY_DELETED, ErrorMessagesService.getMessage(Key.ALREADY_DELETED));
		}
		
		if(user.getId() != comment.getSender().getId() && !(user.getRole().equals(UserRole.ADMIN))) {
			logger.info("Exception cause, {} and {}", (user.getId() == comment.getSender().getId()), (user.getRole().equals(UserRole.ADMIN)));
			throw new ParanormalException(ErrorCode.UNAUTHORIZED, ErrorMessagesService.getMessage(Key.UNAUTHORIZED));
		}
		
		comment.setDeleted(true);
		this.commentRepository.save(comment);
		return true;
	}

	/*public Boolean adminDeleteById(Long id) {
		User user = this.userService.getLoggedInUser();
		Comment comment = this.findById(id);
		if(comment.getDeleted()) {
			throw new ParanormalException(ErrorCode.ALREADY_DELETED, ErrorMessagesService.getMessage(Key.ALREADY_DELETED));
		}
		if(!user.getRole().equals(UserRole.ADMIN)) {
			throw new ParanormalException(ErrorCode.FORBIDDEN, ErrorMessagesService.getMessage(Key.FORBIDDEN));
		}
		comment.setDeleted(true);
		this.commentRepository.save(comment);
		return true;
	}*/
}
