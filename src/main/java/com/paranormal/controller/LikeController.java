package com.paranormal.controller;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paranormal.dto.response.LikeResponse;
import com.paranormal.entity.like.Like;
import com.paranormal.service.LikeService;

@RestController
@RequestMapping("/api/like")
public class LikeController {

	@Autowired
	private LikeService likeService;
	
	
	@PostMapping("/comment")
	public LikeResponse likeComment(@PathParam("id") Long id) {
		return this.likeService.likeComment(id);
	}
	
	@PostMapping("/post")
	public LikeResponse likePost(@PathParam("id") Long id) {
		return this.likeService.likePost(id);
	}
	
}
