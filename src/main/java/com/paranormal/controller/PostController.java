package com.paranormal.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paranormal.dto.request.PostCreationRequest;
import com.paranormal.dto.response.PostResponse;
import com.paranormal.entity.post.Post;
import com.paranormal.service.PostService;

@RestController
@RequestMapping("/api/post")
public class PostController {

	@Autowired
	private PostService postService;
	
	@PostMapping("/create")
	public Post create(@RequestBody PostCreationRequest request) {
		return this.postService.createPost(request);
	}
	
	@GetMapping
	public PostResponse findById(@PathParam("id") Long id) {
		return this.postService.findByIdWithComments(id);
	}
	
	@GetMapping("/all")
	public List<Post> all(){
		return this.postService.all();
	}
	
	
}
