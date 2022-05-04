package com.paranormal.test.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

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

import com.paranormal.dto.request.CommentCreationRequest;
import com.paranormal.dto.response.CommentResponse;
import com.paranormal.dto.response.ErrorModel;
import com.paranormal.entity.comment.Comment;
import com.paranormal.service.ErrorMessagesService;
import com.paranormal.service.ErrorMessagesService.Key;
import com.paranormal.test.helper.CommentHelper;
import com.paranormal.test.helper.LoginHelper;
import com.paranormal.test.helper.PostHelper;
import com.paranormal.test.helper.UserHelper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = { "/cleanup.sql", "/data.sql" })
public class CommentControllerIT {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	@DisplayName(value = "Comment Creation Test")
	void given_comment_creation_request_when_comment_create_then_it_should_return_200_and_comment() {
		String token = LoginHelper.loginWithDefaultUser(this.restTemplate);
		Long postId = 2L;
		CommentCreationRequest request = new CommentCreationRequest();
		request.setContent("comment content");

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);

		final HttpEntity<CommentCreationRequest> entity = new HttpEntity<>(request, headers);
		final ResponseEntity<CommentResponse> response = this.restTemplate.exchange("/api/comment/create?id=" + postId,
				HttpMethod.POST, entity, new ParameterizedTypeReference<CommentResponse>() {
				});

		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(response.getStatusCodeValue(), 200);
		assertEquals(response.getBody().getContent(), request.getContent());
		assertEquals(response.getBody().getSender().getUsername(), UserHelper.getUsername());
	}

	@Test
	@DisplayName(value = "Test for Creating Comment with Invalid Post ID")
	void given_invalid_post_id_when_create_comment_with_post_id_then_it_should_return_410_and_error_model() {
		String token = LoginHelper.loginWithDefaultUser(restTemplate);
		Long postId = 0L;
		CommentCreationRequest request = new CommentCreationRequest();
		request.setContent("comment content");

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);

		final HttpEntity<CommentCreationRequest> entity = new HttpEntity<>(request, headers);
		final ResponseEntity<ErrorModel> response = this.restTemplate.exchange("/api/comment/create?id=" + postId,
				HttpMethod.POST, entity, new ParameterizedTypeReference<ErrorModel>() {
				});

		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(response.getBody().getStatusCode(), 410);
		assertEquals(response.getBody().getMessage(), ErrorMessagesService.getMessage(Key.NO_RESOURCE));
		assertEquals(response.getBody().getErrorCode(), "NO_RESOURCE");
	}

	@Test
	@DisplayName("Test for Creating Comment without User")
	void given_unathorized_user_when_create_comment_then_it_should_return_401_and_error_model() {
		Long postId = 2L;
		CommentCreationRequest request = new CommentCreationRequest();
		request.setContent("aaaaaaaaaaaaaaaa");

		final HttpEntity<CommentCreationRequest> entity = new HttpEntity<>(request);
		final ResponseEntity<ErrorModel> response = this.restTemplate.exchange("/api/comment/create?id=" + postId,
				HttpMethod.POST, entity, new ParameterizedTypeReference<ErrorModel>() {
				});

		assertNotNull(response.getBody());
		assertEquals(response.getBody().getStatusCode(), 401);
		assertEquals(response.getBody().getMessage(), ErrorMessagesService.getMessage(Key.UNAUTHORIZED));
		assertEquals(response.getBody().getErrorCode(), "UNAUTHORIZED");
	}

	@Test
	@DisplayName(value = "Test for Find Comment with invalid Comment ID")
	void given_invalid_comment_id_when_find_comment_then_it_should_return_410_and_error_model() {
		Long commentId = 0L;

		final ResponseEntity<ErrorModel> response = this.restTemplate.getForEntity("/api/comment?id=" + commentId,
				ErrorModel.class);

		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(response.getBody().getStatusCode(), 410);
		assertEquals(response.getBody().getMessage(), ErrorMessagesService.getMessage(Key.NO_RESOURCE));
		assertEquals(response.getBody().getErrorCode(), "NO_RESOURCE");
	}

	@Test
	@DisplayName(value = "Test for Find Comment with ID")
	void given_comment_id_when_find_comment_then_it_should_return_200_and_comment_response() {
		Long commentId = 3L;

		final ResponseEntity<CommentResponse> response = this.restTemplate.getForEntity("/api/comment?id=" + commentId,
				CommentResponse.class);

		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(response.getStatusCodeValue(), 200);
		assertEquals(response.getBody().getContent(), CommentHelper.CONTENT);
		assertEquals(response.getBody().getSender().getUsername(), "crazy_boy");
	}
}
