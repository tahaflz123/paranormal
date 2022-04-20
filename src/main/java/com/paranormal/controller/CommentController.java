package com.paranormal.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paranormal.dto.request.CommentCreationRequest;
import com.paranormal.entity.comment.Comment;
import com.paranormal.service.CommentService;

@RestController
@RequestMapping("/api/comment")
public class CommentRepository {

	@Autowired
	private CommentService commentService;
	
	
	@PostMapping("/create")
	public Comment createComment(@PathParam("id") Long id, @RequestBody CommentCreationRequest request) {
		return this.commentService.createComment(id, request);
	}
	
	@GetMapping("/all")
	public List<Comment> findAll(){
		return this.commentService.findAll();
	}
	
}
