package com.paranormal.test.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import com.paranormal.dto.response.ErrorModel;
import com.paranormal.dto.response.LikeResponse;
import com.paranormal.exception.ErrorCode;
import com.paranormal.service.ErrorMessagesService;
import com.paranormal.service.ErrorMessagesService.Key;
import com.paranormal.test.helper.CommentHelper;
import com.paranormal.test.helper.LoginHelper;
import com.paranormal.test.helper.PostHelper;
import com.paranormal.test.helper.UserHelper;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/cleanup.sql", "/data.sql"})
public class LikeControllerIT {

	@Autowired
	private TestRestTemplate restTemplate;

	
	
	@Test
	@DisplayName(value = "Test for Post")
	void given_post_id_when_like_post_then_it_should_return_200_and_like_response() {
		String token = LoginHelper.loginWithDefaultUser(restTemplate);
		Long postId = 2L;

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		final HttpEntity<String> entity = new HttpEntity<>(headers);
		final ResponseEntity<LikeResponse> firstLikeResponse = this.restTemplate.exchange("/api/like/post?id=" + postId,HttpMethod.POST, entity, new ParameterizedTypeReference<LikeResponse>() {});
		
		assertNotNull(firstLikeResponse);
		assertEquals(firstLikeResponse.getStatusCodeValue(), 200);
		assertNotNull(firstLikeResponse.getBody());
		assertNull(firstLikeResponse.getBody().getCommentResponse());
		assertEquals(firstLikeResponse.getBody().getPostResponse().getHeader(), PostHelper.getHeader());
		assertEquals(firstLikeResponse.getBody().getPostResponse().getContent(), PostHelper.getContent());
		assertEquals(firstLikeResponse.getBody().getUserResponse().getUsername(), UserHelper.getUsername());
		
		final ResponseEntity<LikeResponse> secondLikeResponse = this.restTemplate.exchange("/api/like/post?id=" + postId,HttpMethod.POST, entity, new ParameterizedTypeReference<LikeResponse>() {});
		assertNotNull(secondLikeResponse);
		assertEquals(secondLikeResponse.getStatusCodeValue(), 200);
		assertNull(secondLikeResponse.getBody());
	}
	
	@Test
	@DisplayName(value = "Test for Comment")
	void given_comment_id_when_like_post_then_it_should_return_200_and_like_response() {
		String token = LoginHelper.loginWithDefaultUser(restTemplate);
		Long commentId = 3L;

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		final HttpEntity<String> entity = new HttpEntity<>(headers);
		final ResponseEntity<LikeResponse> firstLikeResponse = this.restTemplate.exchange("/api/like/comment?id=" + commentId,HttpMethod.POST, entity, new ParameterizedTypeReference<LikeResponse>() {});
		
		assertNotNull(firstLikeResponse);
		assertEquals(firstLikeResponse.getStatusCodeValue(), 200);
		assertNotNull(firstLikeResponse.getBody());
		assertNotNull(firstLikeResponse.getBody().getCommentResponse());
		assertNull(firstLikeResponse.getBody().getPostResponse());
		assertNotNull(firstLikeResponse.getBody().getUserResponse());
		assertEquals(firstLikeResponse.getBody().getCommentResponse().getContent(), CommentHelper.CONTENT);
		assertEquals(firstLikeResponse.getBody().getUserResponse().getUsername(), UserHelper.getUsername());
		
		final ResponseEntity<LikeResponse> secondLikeResponse = this.restTemplate.exchange("/api/like/comment?id=" + commentId,HttpMethod.POST, entity, new ParameterizedTypeReference<LikeResponse>() {});
		assertNotNull(secondLikeResponse);
		assertEquals(secondLikeResponse.getStatusCodeValue(), 200);
		assertNull(secondLikeResponse.getBody());
	}
	
	@Test
	@DisplayName(value = "Test with invalid Post ID")
	void given_invalid_post_id_when_like_post_then_it_should_return_410_and_error_model() {
		String token = LoginHelper.loginWithDefaultUser(restTemplate);
		Long postId = 0L;
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Autohorization", "Bearer " + token);
		final HttpEntity<String> entity = new HttpEntity<>(headers);
		final ResponseEntity<ErrorModel> response = this.restTemplate.exchange("/api/like/post?id=" + postId, HttpMethod.POST,entity, new ParameterizedTypeReference<ErrorModel>() {});
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(response.getBody().getErrorCode(), "NO_RESOURCE");
		assertEquals(response.getBody().getStatusCode(), 410);
		assertEquals(response.getBody().getMessage(), ErrorMessagesService.getMessage(Key.NO_RESOURCE));
	}
	
	@Test
	@DisplayName(value = "Test with invalid Comment ID")
	void given_invalid_comment_id_when_like_post_then_it_should_return_410_and_error_model() {
		String token = LoginHelper.loginWithDefaultUser(restTemplate);
		Long commentId = 0L;
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		final HttpEntity<String> entity = new HttpEntity<>(headers);
		final ResponseEntity<ErrorModel> response = this.restTemplate.exchange("/api/like/comment?id=" + commentId, HttpMethod.POST,entity, new ParameterizedTypeReference<ErrorModel>() {});
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(response.getBody().getErrorCode(), "NO_RESOURCE");
		assertEquals(response.getBody().getStatusCode(), 410);
		assertEquals(response.getBody().getMessage(), ErrorMessagesService.getMessage(Key.NO_RESOURCE));
	}
	
	@Test
	@DisplayName(value = "Test for Comment without User")
	void given_comment_id_without_user_when_create_comment_then_it_should_return_401_and_error_model() {
		Long commentId = 3L;
		
		ResponseEntity<ErrorModel> response = this.restTemplate.postForEntity("/api/like/comment?id=" + commentId, null, ErrorModel.class);
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(response.getBody().getStatusCode(), 401);
		assertEquals(response.getBody().getMessage(), ErrorMessagesService.getMessage(Key.UNAUTHORIZED));
		assertEquals(response.getBody().getErrorCode(), "UNAUTHORIZED");
	}
	
	@Test
	@DisplayName(value = "Test for Post without User")
	void given_post_id_without_user_when_create_comment_then_it_should_return_401_and_error_model() {
		Long postId = 2L;
		
		ResponseEntity<ErrorModel> response = this.restTemplate.postForEntity("/api/like/post?id=" + postId, null, ErrorModel.class);
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(response.getBody().getStatusCode(), 401);
		assertEquals(response.getBody().getMessage(), ErrorMessagesService.getMessage(Key.UNAUTHORIZED));
		assertEquals(response.getBody().getErrorCode(), "UNAUTHORIZED");
	}
	
}