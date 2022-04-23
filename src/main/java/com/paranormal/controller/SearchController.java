package com.paranormal.controller;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paranormal.dto.response.SearchResponse;
import com.paranormal.service.SearchService;

@RestController
@RequestMapping("/api/search")
public class SearchController {

	@Autowired
	private SearchService searchService;
	
	@GetMapping
	public SearchResponse search(@PathParam("q") String q) {
		return this.searchService.search(q);
	}
	
}
