package com.paranormal.test.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paranormal.dto.request.LoginRequest;
import com.paranormal.dto.request.RegistrationRequest;
import com.paranormal.dto.response.ErrorModel;
import com.paranormal.entity.user.User;
import com.paranormal.service.ErrorMessagesService;
import com.paranormal.test.helper.UserHelper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/cleanup.sql", "/data.sql"})
public class UserControllerIT {

	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"/cleanup.sql"})
	@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD ,scripts = {"/cleanup.sql", "/data.sql"})
	void given_registration_request_when_register_then_it_should_return_200_and_user() {
		RegistrationRequest request = new RegistrationRequest();
		request.setEmail(UserHelper.getEmail());
		request.setPassword(UserHelper.getNativePassword());
		request.setUsername(UserHelper.getUsername());
		
		final ResponseEntity<User> response = restTemplate.postForEntity("/api/user/register", request, User.class);
	    User user = response.getBody();
	    
		assertNotNull(response);
	    assertEquals(request.getEmail(), user.getEmail());
	    assertEquals(request.getUsername(), user.getUsername());
	}
	
	@Test
	void given_login_request_when_login_then_it_should_return_200_and_token() {
		LoginRequest request = new LoginRequest();
		request.setEmail(UserHelper.getEmail());
		request.setPassword(UserHelper.getNativePassword());
		
		final ResponseEntity<String> response = this.restTemplate.postForEntity("/api/user/login", request, String.class);
		String token = response.getBody();
		System.err.println(token);
		assertNotNull(response);
		assertNotNull(token);
	}
	
	@Test
	void given_Wrong_Email_in_Login_Request_when_Login_then_Status_401_It_Should_Return_Credentials_Not_Matching_Error() {
		LoginRequest request = new LoginRequest();
		request.setEmail("asdf");
		request.setPassword(UserHelper.getNativePassword());
		
		JsonNode jsonNode = this.objectMapper.valueToTree(request);
		
		final HttpEntity<JsonNode> httpEntity = new HttpEntity<JsonNode>(jsonNode);
		final ResponseEntity<ErrorModel> response = this.restTemplate.exchange("/api/user/login", HttpMethod.POST,httpEntity, new ParameterizedTypeReference<ErrorModel>() {});
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(response.getBody().getStatusCode(), 401);
	}
	
	@Test
	void given_exists_email_when_register_then_return_409_it_should_return_email_already_exists_error() {
		RegistrationRequest request = new RegistrationRequest();
		request.setEmail(UserHelper.getEmail());
		request.setPassword("1234");
		request.setUsername("username");
		
		JsonNode jsonNode = this.objectMapper.valueToTree(request);
		
		final HttpEntity<JsonNode> entity = new HttpEntity<>(jsonNode);
		final ResponseEntity<ErrorModel> response = this.restTemplate.exchange("/api/user/register", HttpMethod.POST, entity, new ParameterizedTypeReference<ErrorModel>() {
				});
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(response.getBody().getStatusCode(), 409);
	}
	
	@Test
	void given_user_id_when_find_by_id_then_it_should_return_200_and_user() {
		Long id = 1L;
		
		final ResponseEntity<User> response = this.restTemplate.getForEntity("/api/user?id=" + id, User.class);
		User user = response.getBody();
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertNotNull(response);
		assertNotNull(user);
		assertEquals(user.getUsername(), UserHelper.getUsername());
	}
	
}
