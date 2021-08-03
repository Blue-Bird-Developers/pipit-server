package com.bluebird.pipit.portal;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "portal.jsoup.connection")
@ConstructorBinding
public class ConnectionProperties {
	private final String url;
	private final String userAgent;
	private final Header header;

	@Getter
	@Setter(AccessLevel.PACKAGE)
	static class Header {
		private String origin;
		private String referer;
		private String accept;
		private String contentType;
		private String acceptEncoding;
		private String acceptLanguage;
	}
}
