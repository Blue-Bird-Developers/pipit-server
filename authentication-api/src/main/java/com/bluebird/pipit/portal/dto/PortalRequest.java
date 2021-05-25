package com.bluebird.pipit.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PortalRequest {
	private final String portalId;
	private final String portalPassword;
}
