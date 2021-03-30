package com.bluebird.pipit.portal;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "connection")
@Getter @Setter
public class ConnectionConfig {
	private String url;
	private String userAgent;
	private Map<String, String> header;
}
