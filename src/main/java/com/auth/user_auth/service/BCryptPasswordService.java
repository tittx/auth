package com.auth.user_auth.service;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BCryptPasswordService {

    public BCryptPasswordService() {}

    @Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public String encodePassword(String password) {
		PasswordEncoder encoder = passwordEncoder();
		return encoder.encode(password);
	} 

	public boolean validateEncodedPassword(String rawPassord, String encodedPassword) {
		PasswordEncoder encoder = passwordEncoder();
		return encoder.matches(rawPassord, encodedPassword);
	}
}