package com.bluebird.pipit.portal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PortalLoginCrawler {
	private static final String JSESSION_ID = "JSESSIONMARKID";
	private static final int CONNECTION_TIMEOUT = 3000;
	private final ConnectionProperties connectionProperties;

	public boolean loginPortal(String id, String password) {
		Connection.Response loginPageResponse;
		try {
			loginPageResponse = Jsoup.connect(connectionProperties.getUrl())
				.timeout(CONNECTION_TIMEOUT)
				.header("Origin", connectionProperties.getHeader().getOrigin())
				.header("Referer", connectionProperties.getHeader().getReferer())
				.header("Accept", connectionProperties.getHeader().getAccept())
				.header("Content-Type", connectionProperties.getHeader().getContentType())
				.header("Accept-Encoding", connectionProperties.getHeader().getAcceptEncoding())
				.header("Accept-Language", connectionProperties.getHeader().getAcceptLanguage())
				.method(Connection.Method.GET)
				.execute();
		} catch (IOException e) {
			log.error("Failed to connect to login page : GET", e);
			return false;
		}

		Map<String, String> loginPageCookie = loginPageResponse.cookies();

		Document loginPageDocument;
		try {
			loginPageDocument = loginPageResponse.parse();
		} catch (IOException e) {
			log.error("Failed to parse response", e);
			return false;
		}
		String j_salt = loginPageDocument.select("input.j_salt").val();

		Map<String, String> loginFormData = new HashMap<>();
		loginFormData.put("login_submit", "on");
		loginFormData.put("login_do_redirect", "1");
		loginFormData.put("no_cert_storing", "on");
		loginFormData.put("j_salt", j_salt);
		loginFormData.put("j_username", id);
		loginFormData.put("j_password", password);
		loginFormData.put("saveid", "N");

		Connection.Response loginResponse;
		try {
			loginResponse = Jsoup.connect(connectionProperties.getUrl())
				.userAgent(connectionProperties.getUserAgent())
				.timeout(CONNECTION_TIMEOUT)
				.header("Origin", connectionProperties.getHeader().getOrigin())
				.header("Referer", connectionProperties.getHeader().getReferer())
				.header("Accept", connectionProperties.getHeader().getAccept())
				.header("Content-Type", connectionProperties.getHeader().getContentType())
				.header("Accept-Encoding", connectionProperties.getHeader().getAcceptEncoding())
				.header("Accept-Language", connectionProperties.getHeader().getAcceptLanguage())
				.cookies(loginPageCookie)
				.data(loginFormData)
				.method(Connection.Method.POST)
				.execute();
		} catch (IOException e) {
			log.error("Failed to connect to login page : POST", e);
			return false;
		}

		return loginResponse.cookies().containsKey(JSESSION_ID);
	}

}
