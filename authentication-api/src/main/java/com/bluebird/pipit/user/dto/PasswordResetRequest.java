package com.bluebird.pipit.user.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PasswordResetRequest {
	@NotNull
	private final boolean authSuccess;
	@NotNull
	private final String pipitId;
	@NotNull
	private final String pipitPassword;
	private final String portalId;
	private final String portalPassword;
}
