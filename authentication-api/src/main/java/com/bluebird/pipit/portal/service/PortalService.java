package com.bluebird.pipit.portal.service;

import org.springframework.stereotype.Service;

import com.bluebird.pipit.portal.PortalLoginCrawler;
import com.bluebird.pipit.portal.dto.PortalRequest;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PortalService {
	private final PortalLoginCrawler portalLoginCrawler;

	public boolean loginPortal(PortalRequest portalRequest) {
		String id = portalRequest.getPortalId();
		String password = portalRequest.getPortalPassword();
		return portalLoginCrawler.loginPortal(id, password);
	}
}
