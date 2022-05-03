package com.paranormal.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paranormal.dto.request.PostCreationRequest;
import com.paranormal.dto.response.CommentResponse;
import com.paranormal.dto.response.PostResponse;
import com.paranormal.entity.comment.Comment;
import com.paranormal.entity.post.Post;
import com.paranormal.entity.user.User;
import com.paranormal.repository.PostRepository;
import com.paranormal.service.CommentService;
import com.paranormal.service.LikeService;
import com.paranormal.service.PostService;
import com.paranormal.service.UserService;
import com.paranormal.test.helper.CommentHelper;
import com.paranormal.test.helper.PostHelper;
import com.paranormal.test.helper.UserHelper;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

	@Mock
	private PostRepository postRepository;
	
	@Mock
	private CommentService commentService;
	
	@Mock
	private UserService userService;
	
	@Mock
	private LikeService likeService;
	
	@InjectMocks
	private PostService postService;
	
	@Test
	@DisplayName("post creation test")
	@Order(1)
	void createPost() {
		PostCreationRequest request = new PostCreationRequest();
		request.setHeader("header");
		request.setContent("content");
		
		User user = UserHelper.createUser();
		
		Post post = PostHelper.createPost();
		
		when(this.postRepository.existsByHeader(post.getHeader())).thenReturn(false);
		when(this.userService.getLoggedInUser()).thenReturn(user);
		when(this.postRepository.save(any(Post.class))).thenReturn(post);
		
		PostResponse postResponse = this.postService.createPost(request);

		InOrder order = inOrder(this.postRepository, this.userService);
		order.verify(this.postRepository).existsByHeader(post.getHeader());
		order.verify(this.userService).getLoggedInUser();
		order.verify(this.postRepository).save(any(Post.class));
		
		assertNotNull(postResponse);
		assertEquals(postResponse.getHeader(), request.getHeader());
		assertEquals(postResponse.getContent(), request.getContent());
		assertEquals(postResponse.getUser().getUsername(), user.getUsername());
	}
	
	@Test
	@DisplayName(value = "find user by id test")
	@Order(2)
	void findPostById() {
		Post post = PostHelper.createPost();
		when(this.postRepository.findById(1L)).thenReturn(Optional.of(post));
		Post response = this.postService.findById(1L);
		
		assertNotNull(response);
		assertEquals(post.getHeader(), response.getHeader());
		assertEquals(post.getContent(), response.getContent());
		assertEquals(post.getCreatedDate(), response.getCreatedDate());
		assertEquals(post.getUser().getUsername(), response.getUser().getUsername());
		assertEquals(post.getUser().getPassword(), response.getUser().getPassword());
		assertEquals(post.getUser().getCreatedDate(), response.getUser().getCreatedDate());
		assertEquals(post.getUser().getEmail(), response.getUser().getEmail());
	}
	
	@Test
	@Order(3)
	void findByIdWithComments() {
		Post post = PostHelper.createPost();
		List<Comment> comments = new ArrayList<>();
		comments.add(CommentHelper.createComment());
		when(this.postRepository.findById(anyLong())).thenReturn(Optional.of(post));
		when(this.commentService.findAllByPostId(anyLong())).thenReturn(comments);
		
		PostResponse postResponse = this.postService.findByIdWithComments(1L);
		List<CommentResponse> commentResponse = CommentService.commentsToResponseList(comments);
		assertNotNull(postResponse);
		assertEquals(post.getContent(), postResponse.getContent());
		assertEquals(post.getHeader(), postResponse.getHeader());
		assertEquals(post.getUser().getUsername(), postResponse.getUser().getUsername());
		assertEquals(commentResponse, postResponse.getComments());
	}
	
	@Test
	@Order(4)
	void findAll() {
		List<Post> posts = new ArrayList<>();
		posts.add(PostHelper.createPost());
		when(this.postRepository.findAll()).thenReturn(posts);
		List<Post> response = this.postService.all();
		assertEquals(posts, response);
	}
	
}
