package com.bluebird.pipit.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "security")
@ConstructorBinding
public class SecurityProperties {
	private final long accessTokenValidSecond;
	private final String jwtCookieName;
	private final String jwtSecret;
}
