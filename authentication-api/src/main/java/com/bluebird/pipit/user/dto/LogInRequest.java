package com.bluebird.pipit.user.dto;

import lombok.Value;

@Value
public class LogInRequest {
	String pipitId;
	private final String pipitPassword;
	private final int deviceId;
	private final int appVersion;
	private final int OSVersion;
	private final String deviceName;
	private final String tokenKey;
}
