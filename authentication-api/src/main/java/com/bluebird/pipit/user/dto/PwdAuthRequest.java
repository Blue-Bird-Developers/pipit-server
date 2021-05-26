package com.bluebird.pipit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PwdAuthRequest {
	private final String pipitId;
	private final String portalId;
	private final String portalPassword;
}
