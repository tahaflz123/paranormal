package com.paranormal.config;

import java.security.Key;
import java.util.Date;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

	private final SecurityConfig securityConfig;
	
	public JWTService(SecurityConfig securityConfig) {
		this.securityConfig = securityConfig;
	}
	
	public String createToken(String email) {
		Date currentDate = new Date();
		return Jwts.builder()
				.signWith(this.getSignKey(), SignatureAlgorithm.HS256)
				.setSubject(email).setExpiration(new Date(currentDate.getTime() + (7 * 24 * 60 * 60 * 100)))
				.setIssuedAt(currentDate)
				.compact();
	}
	
	
	public Authentication parseToken(String token) {
		String subject = Jwts.parserBuilder().setSigningKey(this.getSignKey())
		.build().parseClaimsJws(token).getBody().getSubject();
		return new UsernamePasswordAuthenticationToken(subject, null,null);
	}
	
	
	public Key getSignKey() {
		return Keys.hmacShaKeyFor(securityConfig.getJwtSecret().getBytes());
	}
	
}
