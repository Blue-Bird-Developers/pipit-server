package com.bluebird.pipit.user.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bluebird.pipit.global.util.CookieUtils;
import com.bluebird.pipit.portal.dto.PortalAuthRequest;
import com.bluebird.pipit.portal.service.PortalService;
import com.bluebird.pipit.security.JwtTokenProvider;
import com.bluebird.pipit.security.UserPrincipal;
import com.bluebird.pipit.user.domain.User;
import com.bluebird.pipit.user.dto.LogInRequest;
import com.bluebird.pipit.user.dto.UserCheckRequest;
import com.bluebird.pipit.user.dto.PasswordResetRequest;
import com.bluebird.pipit.user.dto.SignUpRequest;
import com.bluebird.pipit.user.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final AuthenticationManager authenticationManager;
	private final PortalService portalService;

	private static final String COOKIE_NAME = "ACCESS-TOKEN";
	private static final Integer TOKEN_VALID_MILLISECOND = 1000 * 60 * 60 * 24;

	public void signUp(SignUpRequest signUpRequest) {
		Optional<User> userFoundByPipitId = userRepository.findByPipitId(signUpRequest.getPipitId());
		if ( userFoundByPipitId.isPresent() ) {
			throw new RuntimeException("UserID already exists.");
		}

		String encryptedPassword = passwordEncoder.encode(signUpRequest.getPipitPassword());
		User user = User.builder()
			.pipitId(signUpRequest.getPipitId())
			.pipitPassword(encryptedPassword)
			.portalId(signUpRequest.getPortalId())
			.build();
		userRepository.save(user);
	}

	public void logIn(HttpServletResponse response, LogInRequest logInRequest) {
		User user = userRepository.findByPipitId(logInRequest.getPipitId()).orElseThrow(RuntimeException::new);
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(UserPrincipal.create(user),
				logInRequest.getPipitPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtTokenProvider.generateAccessToken(authentication);
		CookieUtils.createCookie(response, COOKIE_NAME, token, true, TOKEN_VALID_MILLISECOND);
	}

	public void logOut(HttpServletRequest request, HttpServletResponse response) {
		CookieUtils.deleteCookie(request, response, COOKIE_NAME);
	}

	public boolean checkForResetPipitPassword(UserCheckRequest userCheckRequest) {
		PortalAuthRequest portalAuthRequest = new PortalAuthRequest(userCheckRequest.getPortalId(), userCheckRequest.getPortalPassword());
		if (portalService.loginPortal(portalAuthRequest)) {
			return userRepository.findByPortalId(portalAuthRequest.getPortalId())
				.map(User::getPipitId)
				.orElseThrow(() -> new RuntimeException("No User matches portal account."))
				.equals(userCheckRequest.getPipitId());
		}
		else
			throw new RuntimeException("Portal authentication failed.");
	}

	public void resetPipitPassword(PasswordResetRequest passwordResetRequest) {
		String pipitId = passwordResetRequest.getPipitId();
		String encryptedPassword = passwordEncoder.encode(passwordResetRequest.getPipitPassword());
		userRepository.findByPipitId(pipitId)
			.map(user -> {
				user.setPipitPassword(encryptedPassword);
				userRepository.save(user);
				Authentication authentication
					= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(UserPrincipal.create(user), encryptedPassword));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				return user;
			})
			.orElseThrow(() -> new RuntimeException("No User matches Pipit ID."));
	}

	public String findPipitId(PortalAuthRequest portalAuthRequest) {
		if (portalService.loginPortal(portalAuthRequest)) {
			return userRepository.findByPortalId(portalAuthRequest.getPortalId())
				.map(User::getPipitId)
				.orElseThrow(() -> new RuntimeException("No Pipit ID matches portal account."));
		}
		else
			throw new RuntimeException("Portal authentication failed.");
	}
}
