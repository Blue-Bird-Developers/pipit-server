package com.bluebird.pipit.domain.user.controller;

import com.bluebird.pipit.domain.user.dao.UserRepository;
import com.bluebird.pipit.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
	private AuthenticationManager authenticationManager;
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private JwtTokenProvider tokenProvider;

	/*
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser() {
		TODO 여기에 cookie 넣어서 Bearer + JWT TOKEN logic 작성
	}
	 */

}
