package com.paranormal.service;



import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.paranormal.config.JWTService;
import com.paranormal.dto.request.LoginRequest;
import com.paranormal.dto.request.RegistrationRequest;
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
				.post(null)
				.build();
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
}
