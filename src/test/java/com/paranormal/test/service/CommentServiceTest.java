package com.paranormal.test.service;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.paranormal.dto.request.CommentCreationRequest;
import com.paranormal.dto.response.CommentResponse;
import com.paranormal.entity.comment.Comment;
import com.paranormal.entity.post.Post;
import com.paranormal.entity.user.User;
import com.paranormal.exception.ParanormalException;
import com.paranormal.repository.CommentRepository;
import com.paranormal.repository.PostRepository;
import com.paranormal.service.CommentService;
import com.paranormal.service.UserService;
import com.paranormal.test.helper.CommentHelper;
import com.paranormal.test.helper.PostHelper;
import com.paranormal.test.helper.UserHelper;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
	
	
	@Mock
	private CommentRepository commentRepository;
	
	@Mock
	private UserService userService;
	
	@Mock
	private PostRepository postRepository;
	
	@InjectMocks
	private CommentService commentService;
	
	/*
	 * public CommentResponse createComment(Long postId, CommentCreationRequest request) {
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
	 * */
	
	
	@Test
	void createComment() {
		Comment comment = CommentHelper.createComment();
		Post post = comment.getPost();
		User user = comment.getSender();
		CommentCreationRequest request = new CommentCreationRequest();
		request.setContent(comment.getContent());
		
		when(this.postRepository.findById(post.getId())).thenReturn(Optional.of(post));
		when(this.userService.getLoggedInUser()).thenReturn(user);
		when(this.commentRepository.save(comment)).thenReturn(comment);
		
		CommentResponse response = this.commentService.createComment(post.getId(), request);
		
		/*assertNotNull(response);
		assertEquals(response.getContent(), comment.getContent());
		assertEquals(response.getPostId(), comment.getPost().getId());
		assertEquals(response.getSender().getId(), comment.getSender().getId());
		assertEquals(response.getSender().getUsername(), comment.getSender().getUsername());*/
	}
	
	
	
	
	
	
	
}
