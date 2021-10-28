package com.bluebird.pipit.user.service;

import com.bluebird.pipit.support.util.CookieUtils;
import com.bluebird.pipit.portal.PortalLoginCrawler;
import com.bluebird.pipit.portal.dto.PortalAuthRequest;
import com.bluebird.pipit.security.JwtTokenProvider;
import com.bluebird.pipit.security.Role;
import com.bluebird.pipit.security.RoleName;
import com.bluebird.pipit.user.domain.User;
import com.bluebird.pipit.user.dto.LogInRequest;
import com.bluebird.pipit.user.dto.PasswordResetRequest;
import com.bluebird.pipit.user.dto.SignUpRequest;
import com.bluebird.pipit.user.dto.UserCheckRequest;
import com.bluebird.pipit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
	private static final String COOKIE_NAME = "ACCESS-TOKEN";
	private static final Integer TOKEN_VALID_MILLISECOND = 1000 * 60 * 60 * 24;

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final PortalLoginCrawler portalLoginCrawler;

	public void signUp(SignUpRequest signUpRequest, boolean isAdminUser) {
		Optional<User> userFoundByPipitId = userRepository.findByPipitId(signUpRequest.getPipitId());
		Optional<User> userFoundByPortalId = userRepository.findByPortalId(signUpRequest.getPortalId());

		if (userFoundByPipitId.isPresent() || userFoundByPortalId.isPresent()) {
			throw new RuntimeException("UserID already exists.");
		}

		String encryptedPassword = passwordEncoder.encode(signUpRequest.getPipitPassword());
		Set<Role> roles = isAdminUser ? Set.of(new Role(RoleName.ROLE_ADMIN)) : Set.of(new Role(RoleName.ROLE_USER));

		User user = User.builder()
			.pipitId(signUpRequest.getPipitId())
			.pipitPassword(encryptedPassword)
			.portalId(signUpRequest.getPortalId())
			.roles(roles)
			.build();
		userRepository.save(user);
	}

	public void login(HttpServletResponse response, LogInRequest logInRequest) {
		User user = userRepository.findByPipitId(logInRequest.getPipitId()).orElseThrow(RuntimeException::new);
		String token = jwtTokenProvider.generateToken(user);
		CookieUtils.createCookie(response, COOKIE_NAME, token, true, TOKEN_VALID_MILLISECOND);
	}

	public void logout(HttpServletRequest request, HttpServletResponse response) {
		CookieUtils.deleteCookie(request, response, COOKIE_NAME);
	}

	public boolean checkForResetPipitPassword(UserCheckRequest userCheckRequest) {
		PortalAuthRequest portalAuthRequest =
			new PortalAuthRequest(userCheckRequest.getPortalId(), userCheckRequest.getPortalPassword());
		String portalId = portalAuthRequest.getPortalId();
		if (portalLoginCrawler.loginPortal(portalId, portalAuthRequest.getPortalPassword())) {
			String pipitIdFoundByPortalId = userRepository.findByPortalId(portalId)
				.map(User::getPipitId)
				.orElseThrow(() -> new RuntimeException("No User matches portal account."));
			return pipitIdFoundByPortalId.equals(userCheckRequest.getPipitId());
		} else {
			throw new RuntimeException("Portal authentication failed.");
		}
	}

	public void resetPipitPassword(PasswordResetRequest passwordResetRequest) {
		String pipitId = passwordResetRequest.getPipitId();
		String encryptedPassword = passwordEncoder.encode(passwordResetRequest.getPipitPassword());
		userRepository.findByPipitId(pipitId)
			.map(user -> {
				user.setPipitPassword(encryptedPassword);
				userRepository.save(user);
				return user;
			})
			.orElseThrow(() -> new RuntimeException("No User matches Pipit ID."));
	}

	public String findPipitId(String portalId, String portalPassword) {
		if (portalLoginCrawler.loginPortal(portalId, portalPassword)) {
			return userRepository.findByPortalId(portalId)
				.map(User::getPipitId)
				.orElseThrow(() -> new RuntimeException("No Pipit ID matches portal account."));
		} else {
			throw new RuntimeException("Portal authentication failed.");
		}
	}

	public void deleteUser(String accessToken, String pipitPassword) {
		User user = userRepository.findById(jwtTokenProvider.getUserIdFromJWT(accessToken)).orElseThrow();

		if (passwordEncoder.matches(pipitPassword, user.getPipitPassword())) {
			userRepository.delete(user);
		} else {
			throw new RuntimeException("Pipit password is incorrect.");
		}
	}
}
