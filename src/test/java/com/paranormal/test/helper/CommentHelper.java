package com.paranormal.test.helper;

import com.paranormal.entity.comment.Comment;

public class CommentHelper {

	public static final String CONTENT = "the best paranormal story ever";
	
	
	public static Comment createComment() {
		return Comment.builder().content(CONTENT).post(PostHelper.createPost()).sender(UserHelper.createUser()).build();
	}
}
