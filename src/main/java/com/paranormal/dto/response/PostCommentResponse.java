package com.paranormal.dto.response;

import java.util.List;

import com.paranormal.entity.comment.Comment;
import com.paranormal.entity.post.Post;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostCommentResponse {

	private PostResponse post;
}
