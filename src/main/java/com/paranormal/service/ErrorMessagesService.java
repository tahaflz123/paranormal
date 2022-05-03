package com.paranormal.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

@Service
public class ErrorMessagesService {

	private static final Map<ErrorMessagesService.Key, String> errorMessages = new HashMap<>();
	
	@PostConstruct
	public void postConstruct() {
		errorMessages.put(Key.EMAIL_ALREADY_EXISTS, "This email already exists.");
		errorMessages.put(Key.UNAUTHORIZED, "Unauthorized user");
		errorMessages.put(Key.TOKEN_EXPIRED, "Token expired, please sign in again");
		errorMessages.put(Key.ACCOUNT_ALREADY_EXISTS, "Account already exists");
		errorMessages.put(Key.CREDENTIALS_NOT_MATCHING, "Credentials not matching.");
		errorMessages.put(Key.POST_HEADER_CONFLICT, "Post is already exists with this header, please change the header");
	    errorMessages.put(Key.NO_RESOURCE, "The following content is deleted or no longer available");
	}
	
	public static String getMessage(Key key) {
		return errorMessages.get(key);
	}
	
	public enum Key{
		UNAUTHORIZED,
		CREDENTIALS_NOT_MATCHING,
		EMAIL_ALREADY_EXISTS,
		POST_HEADER_CONFLICT,
		ACCOUNT_ALREADY_EXISTS,
		NO_RESOURCE,
		TOKEN_EXPIRED
	}
}
