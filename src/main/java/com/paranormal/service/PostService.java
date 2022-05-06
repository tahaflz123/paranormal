package com.paranormal.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.paranormal.dto.request.PostCreationRequest;
import com.paranormal.dto.response.PostResponse;
import com.paranormal.entity.comment.Comment;
import com.paranormal.entity.post.Post;
import com.paranormal.entity.user.User;
import com.paranormal.exception.ErrorCode;
import com.paranormal.exception.ParanormalException;
import com.paranormal.repository.PostRepository;
import com.paranormal.service.ErrorMessagesService.Key;

@Service
public class PostService {

	private PostRepository postRepository;
	private UserService userService;
	private CommentService commentService;
	private LikeService likeService;
	private ErrorMessagesService errorMessagesService;

	@Autowired
	public PostService(LikeService likeService, PostRepository postRepository, 
			UserService userService, CommentService commentService, ErrorMessagesService errorMessagesService) {
		this.postRepository = postRepository;
		this.userService = userService;
		this.commentService = commentService;
		this.likeService = likeService;
		this.errorMessagesService = errorMessagesService;
	}
	
	public PostResponse createPost(PostCreationRequest request) {
		if(postRepository.existsByHeader(request.getHeader())) {
			throw new ParanormalException(ErrorCode.POST_HEADER_CONFLICT, this.errorMessagesService.getMessage(Key.POST_HEADER_CONFLICT));
		}
		User user = this.userService.getLoggedInUser();
		Post post = Post.builder()
				.header(request.getHeader())
				.content(request.getContent())
				.user(user)
				.build();
		post.setCreatedDate(new Date());
		PostResponse response = PostService.postToResponse(this.postRepository.save(post));
		return response;
	}
	
	public Post findById(Long id) {
		return this.postRepository.findById(id)
				.orElseThrow(() -> new ParanormalException(ErrorCode.NO_RESOURCE,
						this.errorMessagesService.getMessage(ErrorMessagesService.Key.NO_RESOURCE)));
	}
	
	// TODO: finds all comments with post id
	public PostResponse findByIdWithComments(Long id) {
		Post post = this.findById(id);
		
		List<Comment> comments = this.commentService.findAllByPostId(id);
		PostResponse response = PostService.postToResponse(post);
		
		if(comments != null) {
			response.setComments(CommentService.commentsToResponseList(comments));
		}
		response.setLikes(this.likeService.countOfPostLikes(id));
		return response;
	}
	
	public List<Post> all(){
		return this.postRepository.findAll();
	}
	
	public static List<PostResponse> postsToResponseList(List<Post> posts){
		List<PostResponse> responses = new ArrayList<PostResponse>();
		if(posts != null) {
			for(Post p : posts) {
				responses.add(PostService.postToResponse(p));
			}
		}
		return responses;
	}
	
	public static PostResponse postToResponse(Post post) {
		if(post == null) {
			return null;
		}
		return PostResponse.builder()
				.id(post.getId())
				.content(post.getContent())
				.header(post.getHeader())
				.user(UserService.userToResponse(post.getUser()))
				.build();
	}

	public List<PostResponse> findAllByPage(int p) {
		Pageable pageable = PageRequest.of(p, 20);
		List<Post> posts = this.postRepository.findAll(pageable).toList();
		List<PostResponse> response = PostService.postsToResponseList(posts);
		for(PostResponse r : response) {
			r.setLikes(this.likeService.countOfPostLikes(r.getId()));
		}
		return response;
	}

	public List<PostResponse> findPostsByHeaderLike(String q) {
		Pageable pageable = PageRequest.of(0, 5);
		List<Post> posts = this.postRepository.findAllByHeaderLike(q, pageable).toList();
		return PostService.postsToResponseList(posts);
	}

	public List<Post> findAllByUserId(Long userId) {
		List<Post> posts = this.postRepository.findAllByUserId(userId);
		return posts;
	}
}