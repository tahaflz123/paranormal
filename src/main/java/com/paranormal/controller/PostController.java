package com.paranormal.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public PostResponse create(@RequestBody PostCreationRequest request) {
		return this.postService.createPost(request);
	}
	
	@GetMapping
	public PostResponse findById(@PathParam("id") Long id) {
		return this.postService.findByIdWithComments(id);
	}
	
	@GetMapping("/all")
	public List<PostResponse> findAllByPage(@RequestParam(value = "p", defaultValue = "0") int p){
		return this.postService.findAllByPage(p);
	}
	
	@DeleteMapping("/delete")
    public Boolean deletByPostId(@PathParam("id") Long id) {
		return this.postService.deletePostById(id);
	}
	
	
}
