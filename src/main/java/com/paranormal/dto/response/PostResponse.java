package com.paranormal.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostResponse {

	private Long id;

	private String header;
	
	private String content;
	
	private UserResponse user;
	
	private List<CommentResponse> comments;
	
	
}
