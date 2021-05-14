package com.bluebird.pipit.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bluebird.pipit.config.SecurityProperties;
import com.bluebird.pipit.global.domain.PipitResponse;
import com.bluebird.pipit.user.dto.LogInRequest;
import com.bluebird.pipit.user.dto.SignUpRequest;
import com.bluebird.pipit.user.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {
	private final UserService userService;
	private final SecurityProperties securityProperties;

	@PostMapping(value = "/signup")
	public ResponseEntity<PipitResponse<Void>> signUp(@RequestBody SignUpRequest signUpRequest) {
		// todo 포털 로그인 인증 필요 -> service 생성
		userService.signUp(signUpRequest);

		return ResponseEntity.ok(new PipitResponse<>(HttpStatus.OK.value(), "OK", null));
	}

	@PostMapping(value = "/login")
	public String logIn(@RequestBody LogInRequest logInRequest) {
		return userService.logIn(logInRequest);
		// return ResponseEntity.ok(new LogInResponse<>(HttpStatus.OK.value(), "OK", jwt, null));
	}

}
