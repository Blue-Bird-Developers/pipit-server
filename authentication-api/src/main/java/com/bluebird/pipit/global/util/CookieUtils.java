package com.bluebird.pipit.global.util;

import java.util.Base64;
import java.util.Collection;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.util.SerializationUtils;

public class CookieUtils {

	public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();

		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return Optional.of(cookie);
				}
			}
		}
		return Optional.empty();
	}

	public static void createCookie(HttpServletResponse response, String name, String token,
		Boolean secure, Integer maxAge) {
		Cookie cookie = new Cookie(name, token);
		cookie.setSecure(secure);
		cookie.setMaxAge(maxAge);
		cookie.setPath("/");
		response.addCookie(cookie);
		addSameSiteCookieAttribute(response);
	}

	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					cookie.setValue("");
					cookie.setPath("/");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}
	}

	private static void addSameSiteCookieAttribute(HttpServletResponse response) {
		Collection<String> headers = response.getHeaders(HttpHeaders.SET_COOKIE);
		boolean firstHeader = true;
		for (String header : headers) { // there can be multiple Set-Cookie attributes
			if (firstHeader) {
				response.setHeader(HttpHeaders.SET_COOKIE, String.format("%s; %s", header, "SameSite=None"));
				firstHeader = false;
				continue;
			}
			response.addHeader(HttpHeaders.SET_COOKIE, String.format("%s; %s", header, "SameSite=Strict"));
		}
	}

	public static String serialize(Object object) {
		return Base64.getUrlEncoder()
			.encodeToString(SerializationUtils.serialize(object));
	}

	public static <T> T deserialize(Cookie cookie, Class<T> tClass) {
		return tClass.cast(SerializationUtils.deserialize(
			Base64.getUrlDecoder().decode(cookie.getValue())));
	}
}
