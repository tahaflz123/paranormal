package com.paranormal.test.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import com.paranormal.dto.request.LoginRequest;

public class LoginHelper {

	static Logger logger = LoggerFactory.getLogger(LoginHelper.class);
	
	public static String loginWithDefaultUser(TestRestTemplate restTemplate){
		LoginRequest request = new LoginRequest();
		request.setEmail(UserHelper.getEmail());
		request.setPassword(UserHelper.getNativePassword());
		ResponseEntity<String> response = restTemplate.postForEntity("/api/user/login", request, String.class);
		logger.info(restTemplate.toString());
		logger.info(response.getBody());
		logger.info("Response -> ", response.toString());
		logger.info(request.toString());
		return response.getBody();
	}
	
	public static String loginWithAdmin(TestRestTemplate restTemplate) {
		LoginRequest request = new LoginRequest();
		request.setEmail("email@paranormal.com");
		request.setPassword(UserHelper.getNativePassword());
		
		ResponseEntity<String> response = restTemplate.postForEntity("/api/user/login", request, String.class);
		logger.info("Response -> {}", response.toString());
		logger.info("Admin Token {}", response.getBody());
		return response.getBody();
	}
	
	public static String loginWithCustomEmail(String email,TestRestTemplate restTemplate) {
		LoginRequest request = new LoginRequest();
		request.setEmail(email);
		request.setPassword(UserHelper.getNativePassword());
		
		final ResponseEntity<String> response = restTemplate.postForEntity("/api/user/login", request, String.class);
	
		logger.info("Custom Email, {}", email);
		logger.info("Custom Login Response, {}", response.toString());
		logger.info("Custom Login Request, {}", response.toString());
		return response.getBody();
	}
}