package com.paranormal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class SecurityConfig {

	@Value("${security.jwtSecret}")
	private String jwtSecret;
	
}
