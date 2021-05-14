package com.bluebird.pipit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignUpRequest {
	private final String portalId;
	private final String portalPassword;
	private final String pipitId;
	private final String pipitPassword;
	private final String displayName;

}
