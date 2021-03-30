package com.bluebird.pipit.portal;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class PortalLoginCrawler {
	private static final String JSESSIONID = "JSESSIONMARKID";
	private final ConnectionConfig connectionConfig;

	public boolean loginPortal(String id, String password) {
		Connection.Response loginPageResponse = null;
		try {
			loginPageResponse = Jsoup.connect(connectionConfig.getUrl())
				.timeout(3000)
				.header("Origin", connectionConfig.getHeader().get("origin"))
				.header("Referer", connectionConfig.getHeader().get("referer"))
				.header("Accept", connectionConfig.getHeader().get("accept"))
				.header("Content-Type", connectionConfig.getHeader().get("contentType"))
				.header("Accept-Encoding", connectionConfig.getHeader().get("acceptEncoding"))
				.header("Accept-Language", connectionConfig.getHeader().get("acceptLanguage"))
				.method(Connection.Method.GET)
				.execute();
		} catch (IOException e) {
			log.error("Failed to connect to login page : GET", e);
		}

		Map<String, String> loginPageCookie = loginPageResponse.cookies();

		Document loginPageDocument = null;
		try {
			loginPageDocument = loginPageResponse.parse();
		} catch (IOException e) {
			log.error("Failed to parse response", e);
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

		Connection.Response loginResponse = null;
		try {
			loginResponse = Jsoup.connect(connectionConfig.getUrl())
				.userAgent(connectionConfig.getUserAgent())
				.timeout(3000)
				.header("Origin", connectionConfig.getHeader().get("origin"))
				.header("Referer", connectionConfig.getHeader().get("referer"))
				.header("Accept", connectionConfig.getHeader().get("accept"))
				.header("Content-Type", connectionConfig.getHeader().get("contentType"))
				.header("Accept-Encoding", connectionConfig.getHeader().get("acceptEncoding"))
				.header("Accept-Language", connectionConfig.getHeader().get("acceptLanguage"))
				.cookies(loginPageCookie)
				.data(loginFormData)
				.method(Connection.Method.POST)
				.execute();
		} catch (IOException e) {
			log.error("Failed to connect to login page : POST", e);
		}

		return loginResponse.cookies().containsKey(JSESSIONID);
	}

}
