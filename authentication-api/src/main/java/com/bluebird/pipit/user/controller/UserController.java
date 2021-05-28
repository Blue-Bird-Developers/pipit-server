package com.bluebird.pipit.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bluebird.pipit.global.domain.PipitResponse;
import com.bluebird.pipit.global.util.CookieUtils;
import com.bluebird.pipit.portal.dto.PortalRequest;
import com.bluebird.pipit.portal.service.PortalService;
import com.bluebird.pipit.user.dto.LogInRequest;
import com.bluebird.pipit.user.dto.PwdAuthRequest;
import com.bluebird.pipit.user.dto.PwdResetRequest;
import com.bluebird.pipit.user.dto.SignUpRequest;
import com.bluebird.pipit.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {
	private final UserService userService;
	private final PortalService portalService;


	@PostMapping(value = "/portal")
	public ResponseEntity<PipitResponse<Void>> portal(@RequestBody PortalRequest portalRequest) {
		if (portalService.loginPortal(portalRequest))
			return ResponseEntity.ok(new PipitResponse<>(HttpStatus.OK.value(), "Portal Login Success", null));
		else
			throw new RuntimeException("Portal authentication failed.");
	}

	@PostMapping(value = "/signup")
	public ResponseEntity<PipitResponse<Void>> signUp(@RequestBody SignUpRequest signUpRequest) {
		if (signUpRequest.isPortalSuccess()) {
			userService.signUp(signUpRequest);
			return ResponseEntity.ok(new PipitResponse<>(HttpStatus.OK.value(), "OK", null));
		}
		else
			throw new RuntimeException("Portal authentication failed.");
	}

	@PostMapping(value = "/login")
	public void logIn(@RequestBody LogInRequest logInRequest, HttpServletResponse httpServletResponse) {
		userService.logIn(httpServletResponse, logInRequest);
	}

	@PostMapping(value = "/logout")
	public void logOut(HttpServletRequest request, HttpServletResponse response) {
		userService.logOut(request, response);
	}

	@PostMapping(value = "/password/auth")
	public ResponseEntity<PipitResponse<Void>> authForResetPwd(@RequestBody PwdAuthRequest pwdAuthRequest) {
		if (userService.authForResetPipitPwd(pwdAuthRequest))
			return ResponseEntity.ok(new PipitResponse<>(HttpStatus.OK.value(), "OK", null));
		else
			throw new RuntimeException("Password authentication failed.");
	}

	@PostMapping(value = "/password/reset")
	public ResponseEntity<PipitResponse<Void>> resetPassword(@RequestBody PwdResetRequest pwdResetRequest) {
		if (pwdResetRequest.isAuthSuccess()) {
			userService.resetPipitPassword(pwdResetRequest);
			return ResponseEntity.ok(new PipitResponse<>(HttpStatus.OK.value(), "OK", null));
		}
		else
			throw new RuntimeException("Failed to reset Pipit password.");
	}

	@PostMapping(value = "/find/id")
	public ResponseEntity<PipitResponse<String>> findId(@RequestBody PortalRequest portalRequest) {
		return ResponseEntity.ok(new PipitResponse<>(HttpStatus.OK.value(), "OK",  userService.findPipitId(portalRequest)));
	}



}
