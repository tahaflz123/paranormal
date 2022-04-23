package com.paranormal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paranormal.dto.response.PostResponse;
import com.paranormal.dto.response.SearchResponse;
import com.paranormal.dto.response.UserResponse;

@Service
public class SearchService {

	
	private PostService postService;
	private UserService userService;
	
	@Autowired
	public SearchService(PostService postService, UserService userService) {
		this.userService = userService;
		this.postService = postService;
	}
	
	
	public SearchResponse search(String q) {
		List<PostResponse> posts = this.postService.findPostsByHeaderLike(q);
		List<UserResponse> users = this.userService.findUsersByUsernameLike(q);
		
		
		return SearchResponse.builder()
				.posts(posts)
				.users(users)
				.build();
	}
	
}
