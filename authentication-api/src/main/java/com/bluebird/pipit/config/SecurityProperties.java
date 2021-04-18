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
	private long accessTokenValidSecond;
	private String accessTokenName;
	private String jwtCookieName;
	private String jwtSecret;
}
