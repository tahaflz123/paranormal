package com.paranormal.test.helper;

import com.paranormal.entity.user.User;

public class UserHelper {

	
	private final static Long ID = 1L;
	private final static String NATIVE_PASSWORD = "123456";
	private final static String USERNAME = "wolf_larsen";
	private final static String PASSWORD = "$2a$10$JdMSNoDmdvZ.sB91Cn8QrOuLn86GQHhdAGSTWVbZOc1INzx.KxnTO";
	private final static String EMAIL = "taha.433@outlook.com";
	
	public static User createUser() {
		User user = User.builder()
				.username(USERNAME)
				.email(EMAIL)
				.password(PASSWORD)
				.build();
		return user;
	}

	public static Long getId() {
		return ID;
	}

	public static String getUsername() {
		return USERNAME;
	}

	public static String getPassword() {
		return PASSWORD;
	}

	public static String getEmail() {
		return EMAIL;
	}
	
	public static String getNativePassword() {
		return NATIVE_PASSWORD;
	}
	
	
}