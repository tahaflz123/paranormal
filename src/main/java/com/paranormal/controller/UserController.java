package com.paranormal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paranormal.dto.request.LoginRequest;
import com.paranormal.dto.request.RegistrationRequest;
import com.paranormal.entity.user.User;
import com.paranormal.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public User register(@RequestBody RegistrationRequest request) {
		return this.userService.register(request);
	}
	
	@PostMapping("/login")
	public String login(@RequestBody LoginRequest request) {
		return this.userService.login(request);
	}
	
	@GetMapping("/all")
	public List<User> findAll(){
		return this.userService.findAll();
	}
	

}
