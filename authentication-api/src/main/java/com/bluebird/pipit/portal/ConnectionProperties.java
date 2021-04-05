package com.bluebird.pipit.portal;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PACKAGE)
@ConfigurationProperties(prefix = "portal.jsoup.connection")
public class ConnectionProperties {
	private String url;
	private String userAgent;
	private Header header;

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
