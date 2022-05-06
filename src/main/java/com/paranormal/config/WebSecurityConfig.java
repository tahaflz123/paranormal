package com.paranormal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
		.csrf().disable()
		.authorizeRequests()
		  .antMatchers("/api/user/register").permitAll()
		  .antMatchers("/api/user/login").permitAll()
		.anyRequest().permitAll()
		.and()
		.cors();
	}

	
	@Bean
	public JWTFilter jwtFilter() {
		return new JWTFilter();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
