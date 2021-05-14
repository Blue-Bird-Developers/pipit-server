package com.bluebird.pipit.user.service;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bluebird.pipit.security.JwtTokenProvider;
import com.bluebird.pipit.security.UserPrincipal;
import com.bluebird.pipit.user.domain.User;
import com.bluebird.pipit.user.dto.LogInRequest;
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

	public void signUp(SignUpRequest signUpRequest) {
		Optional<User> userFoundByPipitId = userRepository.findByPipitId(signUpRequest.getPipitId());
		if ( userFoundByPipitId.isPresent() ) {
			throw new RuntimeException("UserID already exists");
		}

		Optional<User> userFoundByDisplayName = userRepository.findByDisplayName(signUpRequest.getDisplayName());
		if ( userFoundByDisplayName.isPresent() ) {
			throw new RuntimeException("DisplayName already in use");
		}

		String encryptedPassword = passwordEncoder.encode(signUpRequest.getPipitPassword());
		User user = User.builder()
			.pipitId(signUpRequest.getPipitId())
			.pipitPassword(encryptedPassword)
			.displayName(signUpRequest.getDisplayName())
			.build();
		userRepository.save(user);
	}

	// todo return type 협의
	public String logIn(LogInRequest logInRequest) {
		User user = userRepository.findByPipitId(logInRequest.getPipitId()).orElseThrow(RuntimeException::new);
		// todo 비밀번호 검증

		// todo generate token -> cookie
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(UserPrincipal.create(user),
				logInRequest.getPipitPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return jwtTokenProvider.generateAccessToken(authentication);
	}

	// todo 비밀번호 재설정

	// todo 아이디 찾기
}
