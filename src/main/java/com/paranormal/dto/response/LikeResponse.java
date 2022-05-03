package com.paranormal.dto.response;

import lombok.Builder;

import lombok.Data;

@Data
@Builder
public class LikeResponse {

	private UserResponse userResponse;
	
	private PostResponse postResponse;
	
	private CommentResponse commentResponse;
	
}
