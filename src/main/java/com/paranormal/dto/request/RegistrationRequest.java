package com.paranormal.dto.request;

import lombok.Data;

@Data
public class RegistrationRequest {

	private String username;
	
	private String email;
	
	private String password;
	
}
