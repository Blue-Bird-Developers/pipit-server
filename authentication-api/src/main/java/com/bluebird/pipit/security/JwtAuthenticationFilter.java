package com.bluebird.pipit.security;

import com.bluebird.pipit.config.SecurityProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	private JwtTokenProvider tokenProvider;
	private CustomUserDetailsService customUserDetailsService;
	private SecurityProperties securityProperties;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		String jwt = getJwtFromRequest(request);
		if (jwt != null) {
			try {
				if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
					Long userId = tokenProvider.getUserIdFromJWT(jwt);
					UserDetails userDetails = customUserDetailsService.loadUserById(userId);
					UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			} catch (Exception ignored) {

			}
		}
		filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		final Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(securityProperties.getJwtCookieName())) {
				return cookie.getValue().substring(securityProperties.getJwtCookieName().length());
			}
		}
//		return Arrays.stream(request.getCookies())
//			.filter(cookies -> cookies.getName().equals(securityProperties.getJwtCookieName()))
//			.findFirst()
//			.map(Cookie::getValue)
//			.map(cookie -> cookie.substring(securityProperties.getJwtCookieName().length()))
//			.orElse(null);
		return null;
	}
}
