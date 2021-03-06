package com.paranormal.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paranormal.dto.request.CommentCreationRequest;
import com.paranormal.dto.response.CommentResponse;
import com.paranormal.entity.comment.Comment;
import com.paranormal.service.CommentService;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

	@Autowired
	private CommentService commentService;
	
	
	@PostMapping("/create")
	public CommentResponse createComment(@PathParam("id") Long id, @RequestBody CommentCreationRequest request) {
		return this.commentService.createComment(id, request);
	}
	
	@GetMapping
	public CommentResponse findById(@PathParam("id") Long id) {
		return this.commentService.findByIdCommentResponse(id);
	}
	
	@DeleteMapping("/delete")
	public Boolean deleteById(@PathParam("id") Long id) {
		return this.commentService.deleteById(id);
	}
	
	/*@DeleteMapping("/admin/delete")
	public Boolean deleteByAdmin(@PathParam("id") Long id) {
		return this.commentService.adminDeleteById(id);
	}*/
	
}