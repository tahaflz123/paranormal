package com.paranormal.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostResponse {

	private Long id;

	private String header;
	
	private String content;
	
	@JsonIgnoreProperties(value = "posts")
	private UserResponse user;
	
	private List<CommentResponse> comments;
	
	private Integer likes;
}
