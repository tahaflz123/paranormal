package com.paranormal.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchResponse {
	
	private List<UserResponse> users;
	
	private List<PostResponse> posts;

}
