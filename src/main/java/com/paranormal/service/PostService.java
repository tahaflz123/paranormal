package com.paranormal.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paranormal.dto.request.PostCreationRequest;
import com.paranormal.dto.response.PostResponse;
import com.paranormal.entity.comment.Comment;
import com.paranormal.entity.post.Post;
import com.paranormal.exception.AuthenticationException;
import com.paranormal.repository.PostRepository;

@Service
public class PostService {

	private PostRepository postRepository;
	private UserService userService;
	private CommentService commentService;

	@Autowired
	public PostService(PostRepository postRepository, UserService userService, CommentService commentService) {
		this.postRepository = postRepository;
		this.userService = userService;
		this.commentService = commentService;
	}
	
	public Post createPost(PostCreationRequest request) {
		if(postRepository.existsByHeader(request.getHeader())) {
			throw new AuthenticationException("Post already exists");
		}
		Post post = Post.builder()
				.header(request.getHeader())
				.content(request.getContent())
				.user(this.userService.getLoggedInUser())
				.build();
		post.setCreatedDate(new Date());
		return this.postRepository.save(post);
	}
	
	public Post findById(Long id) {
		return this.postRepository.findById(id).get();
	}
	
	public PostResponse findByIdWithComments(Long id) {
		Post post = this.postRepository.findById(id).get();
		for(Comment comment : post.getComment()) {
			comment.getSender().setPosts(null);
		}
		post.getUser().setPosts(null);
		PostResponse response = PostService.postToResponse(post);
		return response;
	}
	
	public List<Post> all(){
		return this.postRepository.findAll();
	}
	
	public static List<PostResponse> postsToResponseList(List<Post> posts){
		List<PostResponse> responses = new ArrayList<PostResponse>();
		if(responses.size() != 0) {
			for(Post p : posts) {
				responses.add(PostService.postToResponse(p));
			}
		}
		return responses;
	}
	
	public static PostResponse postToResponse(Post post) {
		return PostResponse.builder()
				.id(post.getId())
				.comments(CommentService.commentsToResponseList(post.getComment()))
				.content(post.getContent())
				.header(post.getHeader())
				.user(UserService.userToResponse(post.getUser()))
				.build();
	}
}