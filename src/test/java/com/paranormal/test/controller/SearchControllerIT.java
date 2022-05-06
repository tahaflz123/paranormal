package com.paranormal.test.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.paranormal.dto.response.PostResponse;
import com.paranormal.dto.response.SearchResponse;
import com.paranormal.dto.response.UserResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SearchControllerIT {


	@Autowired
	private TestRestTemplate restTemplate;

	
	Logger logger = LoggerFactory.getLogger(SearchControllerIT.class);
	
	@Test
	@DisplayName(value = "Test for Search")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"/cleanup.sql", "/search/searchdata.sql"})
	@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = "/cleanup.sql")
	void given_query_when_search_query_then_it_should_return_200_and_search_response() {
		String q = "a";
		
		ResponseEntity<SearchResponse> response = this.restTemplate.getForEntity("/api/search?q=" + q, SearchResponse.class);

		
		
		
		List<PostResponse> posts = response.getBody().getPosts();
		List<UserResponse> users = response.getBody().getUsers();
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertTrue(posts.size() <= 5);
		assertTrue(users.size() <= 5);
		assertTrue(posts.size() >= 0);
		assertTrue(users.size() >= 0);
		assertTrue(posts.stream().allMatch(post -> post.getHeader().contains(q)));
		assertTrue(users.stream().allMatch(user -> user.getUsername().contains(q)));
	}
	
	
	
}
