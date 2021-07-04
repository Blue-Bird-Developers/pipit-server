package com.bluebird.pipit.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bluebird.pipit.portal.dto.PortalAuthRequest;
import com.bluebird.pipit.portal.service.PortalService;
import com.bluebird.pipit.user.dto.LogInRequest;
import com.bluebird.pipit.user.dto.PasswordResetRequest;
import com.bluebird.pipit.user.dto.SignUpRequest;
import com.bluebird.pipit.user.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {
	private final UserService userService;
	private final PortalService portalService;


	@PostMapping(value = "/portal")
	public void portalAuth(@RequestBody PortalAuthRequest portalAuthRequest) {
		if (!portalService.loginPortal(portalAuthRequest))
			throw new RuntimeException("Portal authentication failed.");
	}

	@PostMapping(value = "/signup")
	public void signUp(@RequestBody SignUpRequest signUpRequest) {
		if (signUpRequest.isPortalSuccess())
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

	@PostMapping(value = "/password/check")
	public void checkForResetPassword(@RequestBody UserCheckRequest userCheckRequest) {
		if (!userService.checkForResetPipitPassword(userCheckRequest))
			throw new RuntimeException("Invalid Pipit Id.");
	}

	@PostMapping(value = "/password/reset")
	public void resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) {
		if (passwordResetRequest.isAuthSuccess())
			userService.resetPipitPassword(passwordResetRequest);
		else
			throw new RuntimeException("Failed to reset Pipit password.");
	}

	@PostMapping(value = "/id/find")
	public String findId(@RequestBody PortalAuthRequest portalAuthRequest) {
		return userService.findPipitId(portalAuthRequest);
	}



}
