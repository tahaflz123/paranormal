package com.paranormal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paranormal.dto.request.PostCreationRequest;
import com.paranormal.entity.post.Post;
import com.paranormal.exception.AuthenticationException;
import com.paranormal.repository.PostRepository;

@Service
public class PostService {

	private PostRepository postRepository;
	private UserService userService;

	@Autowired
	public PostService(PostRepository postRepository, UserService userService) {
		this.postRepository = postRepository;
		this.userService = userService;
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
		return this.postRepository.save(post);
	}
	
	
	public Post findById(Long id) {
		return this.postRepository.findById(id).get();
	}
	
	public List<Post> all(){
		return this.postRepository.findAll();
	}
	
	
}
