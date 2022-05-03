package com.paranormal.service;


import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.paranormal.dto.response.LikeResponse;
import com.paranormal.entity.comment.Comment;
import com.paranormal.entity.like.Like;
import com.paranormal.entity.post.Post;
import com.paranormal.entity.user.User;
import com.paranormal.exception.ErrorCode;
import com.paranormal.exception.ParanormalException;
import com.paranormal.repository.LikeRepository;
import com.paranormal.repository.PostRepository;

@Service
public class LikeService {

	private PostRepository postRepository;
	private CommentService commentService;
	private UserService userService;
	private LikeRepository likeRepository;
	private ErrorMessagesService errorMessagesService;
	
	public LikeService(UserService userService,PostRepository postRepository, 
			CommentService commentService, LikeRepository likeRepository, ErrorMessagesService errorMessagesService) {
		this.userService = userService;
		this.postRepository = postRepository;
		this.commentService = commentService;
		this.likeRepository = likeRepository;
		this.errorMessagesService = errorMessagesService;
	}

	@Transactional
	public LikeResponse likeComment(Long commentId) {
		User user = this.userService.getLoggedInUser();
		Comment comment = this.commentService.findById(commentId);
		if(this.likeRepository.existsByUserIdAndCommentId(user.getId(),commentId)) {
			this.deleteLikeByCommentId(commentId);
			return null;
		}
		Like like = Like.builder()
				.comment(comment)
				.post(null)
				.user(user)
				.build();
		LikeResponse likeResponse = likeToResponse(this.likeRepository.save(like));
		
		return likeResponse;
	}
	
	@Transactional
	public LikeResponse likePost(Long postId) {
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() ->	new ParanormalException(ErrorCode.NO_RESOURCE, this.errorMessagesService.getMessage(ErrorMessagesService.Key.NO_RESOURCE)));
		User user = this.userService.getLoggedInUser();

		boolean exists = this.likeRepository.existsByUserIdAndPostId(user.getId(), postId);
		
		if(exists) {
			this.deleteLikeByPostId(postId);
			return null;
		}
		Like like = this.likeRepository.save(Like.builder()
				.user(user)
				.post(post)
				.comment(null)
				.build());
		LikeResponse likeResponse = likeToResponse(like);
		
		return likeResponse;
	}

	public static LikeResponse likeToResponse(Like like) {
		if(like.getComment() == null) {
			return LikeResponse.builder()
					.commentResponse(null)
					.userResponse(UserService.userToResponse(like.getUser()))
					.postResponse(PostService.postToResponse(like.getPost()))
					.build();
		}else if(like.getPost() == null) {
			return LikeResponse.builder()
					.userResponse(UserService.userToResponse(like.getUser()))
					.postResponse(null)
					.commentResponse(CommentService.commentToResponse(like.getComment()))
					.build();
		}
		return null;
	}
	
	public void deleteLikeByPostId(Long postId) {
		this.likeRepository.deleteByPostId(postId);
	}
	
	public void deleteLikeByCommentId(Long commentId) {
		this.likeRepository.deleteByCommentId(commentId);
	}
	
	public int countOfPostLikes(Long id) {
		return this.likeRepository.countByPostId(id);
	}
	

}
