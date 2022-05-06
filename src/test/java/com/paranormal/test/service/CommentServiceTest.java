package com.paranormal.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paranormal.ParanormalApplication;
import com.paranormal.dto.request.CommentCreationRequest;
import com.paranormal.dto.response.CommentResponse;
import com.paranormal.dto.response.ErrorModel;
import com.paranormal.entity.comment.Comment;
import com.paranormal.entity.post.Post;
import com.paranormal.entity.user.User;
import com.paranormal.exception.ErrorCode;
import com.paranormal.exception.ParanormalException;
import com.paranormal.repository.CommentRepository;
import com.paranormal.repository.PostRepository;
import com.paranormal.service.CommentService;
import com.paranormal.service.ErrorMessagesService;
import com.paranormal.service.UserService;
import com.paranormal.service.ErrorMessagesService.Key;
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
	
	@Test
	@DisplayName(value = "Test for Comment Creation")
	void given_comment_when_create_comment_then_it_should_return_comment() {
		Comment comment = CommentHelper.createComment();
		Post post = comment.getPost();
		User user = comment.getSender();
		CommentCreationRequest request = new CommentCreationRequest();
		request.setContent(comment.getContent());
		
		when(this.postRepository.findById(post.getId())).thenReturn(Optional.of(post));
		when(this.userService.getLoggedInUser()).thenReturn(user);
		when(this.commentRepository.save(any(Comment.class))).thenReturn(comment);
		
		CommentResponse response = this.commentService.createComment(post.getId(), request);
		
		assertNotNull(response);
		assertEquals(response.getContent(), comment.getContent());
		assertEquals(response.getPostId(), comment.getPost().getId());
		assertEquals(response.getSender().getId(), comment.getSender().getId());
		assertEquals(response.getSender().getUsername(), comment.getSender().getUsername());
	}
	
	@Test
	@DisplayName(value = "Test for Comment to Response method for null comment")
	void given_null_comment_when_comment_to_comment_response_then_it_should_return_null() {
		Comment comment = null;
		
		CommentResponse response = CommentService.commentToResponse(comment);
		
		assertNull(response);
	}
	
	@Test
	void when_find_all_then_it_should_return_comment_response_list() {
		List<Comment> comments = new ArrayList<>();
		comments.add(CommentHelper.createComment());
		comments.add(CommentHelper.createComment());
		comments.add(CommentHelper.createComment());
		
		when(this.commentRepository.findAll()).thenReturn(comments);
		
		List<Comment> response = this.commentService.findAll();
		
		assertNotNull(response);
		assertEquals(response.get(0).getContent(), CommentHelper.CONTENT);
		assertEquals(response.get(0).getSender().getUsername(), UserHelper.getUsername());
		assertEquals(response.get(0).getSender().getEmail(), UserHelper.getEmail());
		assertEquals(response.get(0).getSender().getPassword(), UserHelper.getPassword());
		assertEquals(response.get(0).getPost().getHeader(), PostHelper.getHeader());
	}
	
	void given_comment_id_when_find_comment_by_id_then_return_comment() {
		Comment comment = CommentHelper.createComment();
		
		when(this.commentRepository.findById(1L)).thenReturn(Optional.of(comment));
		
		Comment response = this.commentService.findById(1L);
		
		assertNotNull(response);
		assertEquals(comment.getContent(), response.getContent());
		assertEquals(comment.getSender(), response.getSender());
		assertEquals(comment.getSender().getUsername(), response.getSender().getUsername());
		assertEquals(comment.getSender().getPassword(), response.getSender().getUsername());
		assertEquals(comment.getSender().getEmail(), response.getSender().getEmail());
		assertEquals(comment.getPost().getHeader(), response.getPost().getHeader());
	}
}
