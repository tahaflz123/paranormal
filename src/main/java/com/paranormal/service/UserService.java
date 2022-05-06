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
import com.paranormal.entity.post.Post;
import com.paranormal.entity.user.User;
import com.paranormal.entity.user.UserRole;
import com.paranormal.exception.ErrorCode;
import com.paranormal.exception.ParanormalException;
import com.paranormal.repository.PostRepository;
import com.paranormal.repository.UserRepository;
import com.paranormal.service.ErrorMessagesService.Key;

@Service
public class UserService {
	
	Logger logger = Logger.getLogger("user-service-logger");

	private UserRepository userRepository;
	private JWTService jwtService;
	private BCryptPasswordEncoder passwordEncoder;
	private PostRepository postRepository;
	private ErrorMessagesService errorMessagesService;
	
	@Autowired
	public UserService(PostRepository postRepository, UserRepository userRepository,
			JWTService jwtService, BCryptPasswordEncoder passwordEncoder, ErrorMessagesService errorMessagesService) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
		this.jwtService = jwtService;
		this.passwordEncoder = passwordEncoder;
		this.errorMessagesService = errorMessagesService;
	}
	
	public User register(RegistrationRequest request) {
		Boolean exists = this.userRepository.existsByEmailOrUsername(request.getEmail(),request.getUsername());
		if(exists) {
			throw new ParanormalException(ErrorCode.EMAIL_ALREADY_EXISTS, this.errorMessagesService.getMessage(ErrorMessagesService.Key.EMAIL_ALREADY_EXISTS));
		}
		logger.info(request.toString());
		User user = User.builder()
				.username(request.getUsername())
				.email(request.getEmail())
				.password(this.passwordEncoder.encode(request.getPassword()))
				.role(UserRole.USER)
				.build();
		user.setCreatedDate(new Date());
		return this.userRepository.save(user);
	}
	
	public String login(LoginRequest request) {
		User user = this.userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new ParanormalException(ErrorCode.CREADENTIALS_NOT_MATCHING, 
						this.errorMessagesService.getMessage(ErrorMessagesService.Key.CREDENTIALS_NOT_MATCHING)));
		
		if(!this.passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new ParanormalException(ErrorCode.CREADENTIALS_NOT_MATCHING, this.errorMessagesService.getMessage(ErrorMessagesService.Key.CREDENTIALS_NOT_MATCHING));
		}
		
		return this.jwtService.createToken(user.getEmail());
	}
	
	public User getLoggedInUser() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return this.userRepository.findByEmail(email)
				.orElseThrow(() -> new ParanormalException(ErrorCode.UNAUTHORIZED, 
						this.errorMessagesService.getMessage(ErrorMessagesService.Key.UNAUTHORIZED)));
	}
	
	public List<User> findAll(){
		return this.userRepository.findAll();
	}
	
	public User findById(Long id) {
		return this.userRepository.findById(id).orElseThrow(() -> 
		new ParanormalException(ErrorCode.NO_RESOURCE, ErrorMessagesService.getMessage(Key.NO_RESOURCE)));
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
		return UserResponse.builder()
				.id(user.getId())
				.username(user.getUsername())
				.build();
	}
	
	//TODO: Finds all posts with user id
	public UserResponse findUserByIdWithPosts(Long id) {
		User user = this.userRepository.findById(id)
				.orElseThrow(() -> new ParanormalException(ErrorCode.NO_RESOURCE, 
						this.errorMessagesService.getMessage(ErrorMessagesService.Key.NO_RESOURCE)));
		
		List<Post> posts = this.postRepository.findAllByUserId(id);
		
		UserResponse response = UserResponse.builder()
				.username(user.getUsername())
				.id(user.getId())
				.build();
		
		if(posts != null) {
			response.setPosts(PostService.postsToResponseList(posts));
		}
		
		return response;
	}

	public List<UserResponse> findUsersByUsernameLike(String q) {
		Pageable pageable = PageRequest.of(0, 5);
		List<User> users = this.userRepository.findAllByUsernameLike(q, pageable).toList();
		return UserService.usersToResponseList(users);
	}

	public Boolean deleteUser(Long id) {
		User user = this.getLoggedInUser();
		
		if(user.getDeleted()) {
			throw new ParanormalException(ErrorCode.ALREADY_DELETED, ErrorMessagesService.getMessage(Key.ALREADY_DELETED));
		}
		
		if(!(user.getId() == id)) {
			throw new ParanormalException(ErrorCode.UNAUTHORIZED, ErrorMessagesService.getMessage(Key.UNAUTHORIZED));
		}
		user.setDeleted(true);
		this.userRepository.save(user);
		return true;
	}
	
	public Boolean deleteUserAsAdmin(Long id) {
		User admin = this.getLoggedInUser();
		User user = this.findById(id);
		
		if(user.getDeleted()) {
			throw new ParanormalException(ErrorCode.ALREADY_DELETED, ErrorMessagesService.getMessage(Key.ALREADY_DELETED));
		}
		
		if(!(admin.getRole().equals(UserRole.ADMIN))) {
			throw new ParanormalException(ErrorCode.UNAUTHORIZED, ErrorMessagesService.getMessage(Key.UNAUTHORIZED));
		}
		
		user.setDeleted(true);
		this.userRepository.save(user);
		return true;
	}
}
