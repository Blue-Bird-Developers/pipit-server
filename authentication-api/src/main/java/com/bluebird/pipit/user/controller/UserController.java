package com.bluebird.pipit.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bluebird.pipit.portal.PortalLoginCrawler;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bluebird.pipit.portal.dto.PortalAuthRequest;
import com.bluebird.pipit.user.dto.LogInRequest;
import com.bluebird.pipit.user.dto.PasswordResetRequest;
import com.bluebird.pipit.user.dto.SignUpRequest;
import com.bluebird.pipit.user.dto.UserCheckRequest;
import com.bluebird.pipit.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "User")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {
	private final UserService userService;
	private final PortalLoginCrawler portalLoginCrawler;


	@ApiOperation(value = "숙명포털 인증", notes = "숙명포털에 로그인을 시도합니다.")
	@PostMapping(value = "/portal")
	public void portalAuth(@RequestBody PortalAuthRequest portalAuthRequest) {
		if (!portalLoginCrawler.loginPortal(portalAuthRequest.getPortalId(), portalAuthRequest.getPortalPassword()))
			throw new RuntimeException("Portal authentication failed.");
	}

	@ApiOperation(value = "회원가입", notes = "회원가입을 통해 피핏 계정을 생성합니다.")
	@PostMapping(value = "/signup")
	public void signUp(@RequestBody SignUpRequest signUpRequest) {
		if (signUpRequest.isPortalSuccess())
			userService.signUp(signUpRequest);
		else
			throw new RuntimeException("Portal authentication failed.");
	}

	@ApiOperation(value = "로그인", notes = "피핏 계정으로 로그인합니다.")
	@PostMapping(value = "/login")
	public void login(@RequestBody LogInRequest logInRequest, HttpServletResponse httpServletResponse) {
		userService.login(httpServletResponse, logInRequest);
	}

	@ApiOperation(value = "로그아웃", notes = "피핏 계정을 로그아웃합니다.")
	@PostMapping(value = "/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		userService.logout(request, response);
	}

	@ApiOperation(value = "계정 확인", notes = "비밀번호 변경에 앞서, 숙명포털 계정 및 피핏 아이디에 해당하는 피핏 계정이 있는지 확인합니다.")
	@PostMapping(value = "/password/check")
	public void checkForResetPassword(@RequestBody UserCheckRequest userCheckRequest) {
		if (!userService.checkForResetPipitPassword(userCheckRequest))
			throw new RuntimeException("Invalid Pipit Id.");
	}

	@ApiOperation(value = "비밀번호 변경", notes = "피핏 아이디에 해당하는 피핏 계정의 비밀번호를 변경합니다.")
	@PostMapping(value = "/password/reset")
	public void resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) {
		if (passwordResetRequest.isAuthSuccess())
			userService.resetPipitPassword(passwordResetRequest);
		else
			throw new RuntimeException("Failed to reset Pipit password.");
	}

	@ApiOperation(value = "아이디 찾기", notes = "숙명포털 계정에 해당하는 피핏 아이디가 있다면 반환합니다.")
	@GetMapping(value = "/id/find")
	public String findId(@RequestParam String portalId, @RequestParam String portalPassword) {
		return userService.findPipitId(portalId, portalPassword);
	}



}
