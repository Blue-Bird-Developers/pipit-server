package com.bluebird.pipit.user.controller;

import com.bluebird.pipit.user.dto.SignUpRequest;
import com.bluebird.pipit.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "AdminUser")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin")
public class AdminController {
	private final UserService userService;

	@ApiOperation(value = "회원가입", notes = "회원가입을 통해 마스터 피핏 계정을 생성합니다.")
	@PostMapping(value = "/signup")
	public void signUp(@RequestBody SignUpRequest signUpRequest) {
		if (signUpRequest.isPortalSuccess()) {
			userService.signUp(signUpRequest, true);
		} else {
			throw new RuntimeException("Admin Portal authentication failed.");
		}
	}

}
