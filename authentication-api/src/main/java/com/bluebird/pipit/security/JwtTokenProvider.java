package com.bluebird.pipit.security;

import com.bluebird.pipit.config.SecurityProperties;
import com.bluebird.pipit.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@AllArgsConstructor
@Component
public class JwtTokenProvider {

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
	private final SecurityProperties securityProperties;

	public String generateToken(User user) {
		Date expiryDate = new Date(System.currentTimeMillis() + securityProperties.getAccessTokenValidSecond());
		Claims claims = Jwts.claims().setSubject(Long.toString(user.getId()));
		claims.put("roles", user.getRoles());

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(expiryDate)
			.signWith(SignatureAlgorithm.HS512, securityProperties.getJwtSecret())
			.compact();
	}

	public Long getUserIdFromJWT(String token) {
		Claims claims = Jwts.parser()
			.setSigningKey(securityProperties.getJwtSecret())
			.parseClaimsJws(token)
			.getBody();

		return Long.parseLong(claims.getSubject());
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(securityProperties.getJwtSecret()).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException ex) {
			logger.error("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			logger.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			logger.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			logger.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			logger.error("JWT claims string is empty.");
		}
		return false;
	}
}
