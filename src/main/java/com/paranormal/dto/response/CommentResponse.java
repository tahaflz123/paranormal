package com.paranormal.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentResponse {

	private Long id;
	
	private String content;
	
	private UserResponse sender;
	
	private Long postId;
	
}
