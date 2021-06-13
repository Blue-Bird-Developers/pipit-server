package com.bluebird.pipit.user.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserCheckRequest {
	@NotNull
	private final String pipitId;
	@NotNull
	private final String portalId;
	@NotNull
	private final String portalPassword;
}
