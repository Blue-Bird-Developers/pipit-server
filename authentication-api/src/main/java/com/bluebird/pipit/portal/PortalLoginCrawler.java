package com.bluebird.pipit.portal;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

public class PortalLoginCrawler {
	String ID;
	String password;

	public PortalLoginCrawler(String ID, String password) {
		this.ID = ID;
		this.password = password;
	}

	public Boolean loginPortal() throws Exception {

		Connection.Response loginPageResponse = Jsoup.connect("https://portal.sookmyung.ac.kr/irj/portal")
			.timeout(3000)
			.header("Origin", "https://portal.sookmyung.ac.kr")
			.header("Referer", "https://portal.sookmyung.ac.kr/irj/portal")
			.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
			.header("Content-Type", "application/x-www-form-urlencoded")
			.header("Accept-Encoding", "gzip, deflate, br")
			.header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
			.method(Connection.Method.GET)
			.execute();

		Map<String, String> loginPageCookie = loginPageResponse.cookies();
		Document loginPageDocument = loginPageResponse.parse();
		String j_salt = loginPageDocument.select("input.j_salt").val();
		String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36";

		Map<String, String> loginFormData = new HashMap<>();
		loginFormData.put("login_submit", "on");
		loginFormData.put("login_do_redirect", "1");
		loginFormData.put("no_cert_storing", "on");
		loginFormData.put("j_salt", j_salt);
		loginFormData.put("j_username", ID);
		loginFormData.put("j_password", password);
		loginFormData.put("saveid", "N");

		Connection.Response response = Jsoup.connect("https://portal.sookmyung.ac.kr/irj/portal")
			.userAgent(userAgent)
			.timeout(3000)
			.header("Origin", "https://portal.sookmyung.ac.kr")
			.header("Referer", "https://portal.sookmyung.ac.kr/irj/portal")
			.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
			.header("Content-Type", "application/x-www-form-urlencoded")
			.header("Accept-Encoding", "gzip, deflate, br")
			.header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
			.cookies(loginPageCookie)
			.data(loginFormData)
			.method(Connection.Method.POST)
			.execute();

		Map<String, String> loginCookie = response.cookies();

		return loginCookie.containsKey("JSESSIONMARKID");
	}

}
