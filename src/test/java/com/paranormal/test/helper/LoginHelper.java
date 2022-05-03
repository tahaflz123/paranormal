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
		logger.info(request.toString());
		return response.getBody();
	}
}
