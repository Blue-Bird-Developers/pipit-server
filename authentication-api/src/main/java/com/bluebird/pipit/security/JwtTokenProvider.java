package com.bluebird.pipit.security;

import com.bluebird.pipit.config.SecurityProperties;
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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@AllArgsConstructor
@Component
public class JwtTokenProvider {

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
	private SecurityProperties securityProperties;

	public String generateAccessToken(Authentication authentication) {
		return generateToken((UserPrincipal) authentication.getPrincipal(),
			securityProperties.getAccessTokenValidSecond());
	}

	public String generateRefreshToken(Authentication authentication) {
		return generateToken((UserPrincipal) authentication.getPrincipal(),
			securityProperties.getRefreshTokenValidSecond());
	}

	public String generateToken(UserPrincipal userPrincipal, long expireTime) {
		Date now = new Date();
		Date expiryDate = new Date(System.currentTimeMillis() + expireTime);

		return Jwts.builder()
			.setSubject(Long.toString(userPrincipal.getId()))
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
