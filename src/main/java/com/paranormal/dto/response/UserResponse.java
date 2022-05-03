package com.paranormal.dto.response;

import java.util.List;

import com.paranormal.entity.post.Post;
import com.paranormal.entity.user.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

	private Long id;
	
	private String username;
	
	private List<PostResponse> posts;

}