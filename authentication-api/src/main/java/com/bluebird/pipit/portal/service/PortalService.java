package com.bluebird.pipit.portal.service;

import org.springframework.stereotype.Service;

import com.bluebird.pipit.portal.PortalLoginCrawler;
import com.bluebird.pipit.portal.dto.PortalAuthRequest;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PortalService {
	private final PortalLoginCrawler portalLoginCrawler;

	public boolean loginPortal(PortalAuthRequest portalAuthRequest) {
		String id = portalAuthRequest.getPortalId();
		String password = portalAuthRequest.getPortalPassword();
		return portalLoginCrawler.loginPortal(id, password);
	}
}
