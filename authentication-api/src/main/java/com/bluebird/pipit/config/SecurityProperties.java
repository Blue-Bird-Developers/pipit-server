package com.bluebird.pipit.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
@Getter
@ToString
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
	private final long accessTokenValidSecond;
	private final String accessTokenName;
	private final String jwtCookieName;
	private final String jwtSecret;
}
