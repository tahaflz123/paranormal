package com.paranormal.test.helper;

import com.paranormal.entity.post.Post;

public class PostHelper {
	
	private static final String HEADER = "the Graveyard";
	private static final String CONTENT = "content of the Graveyard";
	
	public static Post createPost() {
		Post post = Post.builder()
		.header(HEADER)
		.content(CONTENT)
		.user(UserHelper.createUser())
		.build();
		return post;
	}
	
	public static String getHeader() {
		return HEADER;
	}
	
	public static String getContent() {
		return CONTENT;
	}

}
