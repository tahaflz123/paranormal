package com.paranormal.test.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.paranormal.dto.request.PostCreationRequest;
import com.paranormal.dto.response.ErrorModel;
import com.paranormal.dto.response.PostResponse;
import com.paranormal.service.ErrorMessagesService;
import com.paranormal.service.ErrorMessagesService.Key;
import com.paranormal.test.helper.LoginHelper;
import com.paranormal.test.helper.PostHelper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PostControllerIT {
	

	@Autowired
	private TestRestTemplate restTemplate;
	
	private final Logger logger = LoggerFactory.getLogger(PostControllerIT.class);
	
	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"/cleanup.sql", "/data.sql"})
	@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = {"/cleanup.sql", "/data.sql"})
	void given_post_creation_request_when_post_create_then_it_should_return_200_and_post() {
		String token = LoginHelper.loginWithDefaultUser(restTemplate);
		PostCreationRequest request = new PostCreationRequest();
		request.setContent("content");
		request.setHeader("post header");
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		
		final HttpEntity<PostCreationRequest> entity = new HttpEntity<>(request, headers);
		final ResponseEntity<PostResponse> response = restTemplate.exchange("/api/post/create",
                HttpMethod.POST, entity, new ParameterizedTypeReference<PostResponse>() {
                });		PostResponse post = response.getBody();
		
		assertNotNull(response);
		assertEquals(response.getStatusCodeValue(), 200);
		assertNotNull(post);
		assertEquals(request.getContent(), post.getContent());
		assertEquals(request.getHeader(), post.getHeader());
	}
	
	@Test
	void given_exists_post_header_when_create_post_then_it_should_return_409() {
		String token = LoginHelper.loginWithDefaultUser(restTemplate);
		PostCreationRequest request = new PostCreationRequest();
		request.setHeader(PostHelper.getHeader());
		request.setContent(PostHelper.getContent());;
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		
		final HttpEntity<PostCreationRequest> entity = new HttpEntity<>(request, headers);
		final ResponseEntity<ErrorModel> response = this.restTemplate.exchange("/api/post/create", HttpMethod.POST, entity, new ParameterizedTypeReference<ErrorModel>() {
		});
		
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(response.getBody().getStatusCode(), 409);
		assertEquals(response.getBody().getErrorCode(), "POST_HEADER_CONFLICT");
		assertEquals(response.getBody().getMessage(), "Post is already exists with this header, please change the header");
	}
	
	@Test
	void given_post_id_when_find_post_then_it_should_return_200_and_post() {
		Long id = 2L;
		
		ResponseEntity<PostResponse> response = this.restTemplate.getForEntity("/api/post?id=" + id, PostResponse.class);
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(response.getBody().getContent(), "content of the Graveyard");
		assertEquals(response.getBody().getHeader(), "the Graveyard");
	}
	
	@Test
	void given_not_valid_id_when_find_post_then_it_should_return_410() {
		Long id = 0L;
		
		ResponseEntity<ErrorModel> response = this.restTemplate.getForEntity("/api/post?id=" + id, ErrorModel.class);
	
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(response.getBody().getStatusCode(), 410);
		assertEquals(response.getBody().getErrorCode(), "NO_RESOURCE");
		assertEquals(response.getBody().getMessage(), "The following content is deleted or no longer available");
	}
	
	@Test
	void given_post_id_when_delete_post_then_it_should_return_200_and_true(){
		String token = LoginHelper.loginWithDefaultUser(restTemplate);
		Long postId = 14L;
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		
		final HttpEntity<String> entity = new HttpEntity<>(headers);
		final ResponseEntity<Boolean> response = this.restTemplate.exchange("/api/post/delete?id=" + postId, HttpMethod.DELETE,
				entity, new ParameterizedTypeReference<Boolean>() {});
		
		logger.info("[USER] - Post Delete Test, Http Response {} ", response.toString());
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(response.getStatusCodeValue(), 200);
		assertTrue(response.getBody());
	}
	
	@Test
	void given_post_id_when_delete_post_without_sender_user_and_email_then_it_should_return_403_and_error_model(){
		String token = LoginHelper.loginWithDefaultUser(restTemplate);
		Long postId = 13L;
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		
		final HttpEntity<String> entity = new HttpEntity<>(headers);
		final ResponseEntity<ErrorModel> response = this.restTemplate.exchange("/api/post/delete?id=" + postId, HttpMethod.DELETE,
				entity, new ParameterizedTypeReference<ErrorModel>() {});
		
		logger.info("[USER] - Post Delete Test but Post already deleted, Http Response {} ", response.toString());
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(response.getBody().getErrorCode(), Key.FORBIDDEN.name());
		assertEquals(response.getBody().getStatusCode(), 403);
		assertEquals(response.getBody().getMessage(), ErrorMessagesService.getMessage(Key.FORBIDDEN));
	}
	
	@Test
	void given_deleted_post_id_when_delete_post_then_it_should_return_409_and_error_model(){
		String token = LoginHelper.loginWithDefaultUser(restTemplate);
		Long postId = 15L;
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		
		final HttpEntity<String> entity = new HttpEntity<>(headers);
		final ResponseEntity<ErrorModel> response = this.restTemplate.exchange("/api/post/delete?id=" + postId, HttpMethod.DELETE,
				entity, new ParameterizedTypeReference<ErrorModel>() {});
		
		logger.info("[USER] - Post Delete Test but Post already deleted, Http Response {} ", response.toString());
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(response.getBody().getErrorCode(), Key.ALREADY_DELETED.name());
		assertEquals(response.getBody().getStatusCode(), 409);
		assertEquals(response.getBody().getMessage(), ErrorMessagesService.getMessage(Key.ALREADY_DELETED));
	}
	
	@Test
	void given_deleted_post_id_when_delete_post_as_admin_then_it_should_return_409_and_error_model(){
		String token = LoginHelper.loginWithAdmin(restTemplate);
		Long postId = 15L;
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		
		final HttpEntity<String> entity = new HttpEntity<>(headers);
		final ResponseEntity<ErrorModel> response = this.restTemplate.exchange("/api/post/delete?id=" + postId, HttpMethod.DELETE,
				entity, new ParameterizedTypeReference<ErrorModel>() {});
		
		logger.info("[ADMIN] - Post Delete Test but Post already deleted, Http Response {} ", response.toString());
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(response.getBody().getErrorCode(), Key.ALREADY_DELETED.name());
		assertEquals(response.getBody().getStatusCode(), 409);
		assertEquals(response.getBody().getMessage(), ErrorMessagesService.getMessage(Key.ALREADY_DELETED));
	}
	
}
