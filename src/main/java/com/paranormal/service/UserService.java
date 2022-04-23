package com.paranormal.service;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.paranormal.config.JWTService;
import com.paranormal.dto.request.LoginRequest;
import com.paranormal.dto.request.RegistrationRequest;
import com.paranormal.dto.response.UserResponse;
import com.paranormal.entity.user.User;
import com.paranormal.exception.AuthenticationException;
import com.paranormal.repository.UserRepository;

@Service
public class UserService {
	
	Logger logger = Logger.getLogger("user-service-logger");

	private UserRepository userRepository;
	private JWTService jwtService;
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public UserService(UserRepository userRepository, JWTService jwtService, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.jwtService = jwtService;
		this.passwordEncoder = passwordEncoder;
	}
	
	public User register(RegistrationRequest request) {
		Boolean exists = this.userRepository.existsByEmailOrUsername(request.getEmail(),request.getUsername());
		if(exists) {
			throw new AuthenticationException("Email or Username is valid");
		}
		logger.info(request.toString());
		User user = User.builder()
				.username(request.getUsername())
				.email(request.getEmail())
				.password(this.passwordEncoder.encode(request.getPassword()))
				.posts(null)
				.build();
		user.setCreatedDate(new Date());
		return this.userRepository.save(user);
	}
	
	public String login(LoginRequest request) {
		User user = this.userRepository.findByEmail(request.getEmail());
		if(!this.passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new AuthenticationException("Credentials not matching.");
		}
		return this.jwtService.createToken(user.getEmail());
	}
	
	public User getLoggedInUser() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		if(email.isEmpty()) {
			throw new AuthenticationException("You should login for creating post");
		}
		return this.userRepository.findByEmail(email);
	}
	
	public List<User> findAll(){
		return this.userRepository.findAll();
	}
	
	public static List<UserResponse> usersToResponseList(List<User> users){
		List<UserResponse> response = new ArrayList<UserResponse>();
		if(users != null) {
			for(User user : users) {
				response.add(UserService.userToResponse(user));
			}
		}
		return response;
	}
	
	public static UserResponse userToResponse(User user) {
		return UserResponse.builder().id(user.getId()).posts(PostService.postsToResponseList(user.getPosts())).username(user.getUsername()).build();
	}

	public List<UserResponse> findUsersByUsernameLike(String q) {
		Pageable pageable = PageRequest.of(0, 5);
		List<User> users = this.userRepository.findAllByUsernameLike(q, pageable).toList();
		for(User user : users) {
			user.setPosts(null);
		}
		return UserService.usersToResponseList(users);
	}
}
