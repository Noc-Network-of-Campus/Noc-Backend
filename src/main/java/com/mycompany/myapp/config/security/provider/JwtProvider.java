package com.mycompany.myapp.config.security.provider;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.mycompany.myapp.config.security.CustomUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtProvider implements InitializingBean {
	@Value("${jwt.token.secret}")
	private String secret;
	private static SecretKey secretKey;
	private static final Long expireAccessMs = 1000L * 60 * 15; //15분
	private static final Long expireRefreshMs = 1000L * 60 * 60 * 24 * 7; //7일

	@Override
	public void afterPropertiesSet() throws Exception {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		secretKey = Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateToken(Long memberId, String email, Boolean isRegistered) {
		return Jwts.builder()
			.claim("memberId", memberId)
			.claim("email", email)
			.claim("isRegistered", isRegistered)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + expireAccessMs))
			.signWith(secretKey)
			.compact();
	}


	public String generateToken(Long memberId) {
		return Jwts.builder()
			.claim("memberId", memberId)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + expireRefreshMs))
			.signWith(secretKey)
			.compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(secretKey)  // SecretKey는 javax.crypto.SecretKey
			.build()
			.parseClaimsJws(token)
			.getBody();
		Long memberId = claims.get("memberId", Long.class);
		String email = claims.get("email", String.class);
		Boolean isRegistered = claims.get("isRegistered", Boolean.class);

		// Authentication 객체를 생성하여 반환
		UserDetails userDetails = CustomUserDetails.builder()
			.id(memberId)
			.email(email)
			.isRegistered(isRegistered)
			.build();

		List<SimpleGrantedAuthority> authorities = Collections.singletonList(
			new SimpleGrantedAuthority("ROLE_USER")
		);

		return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
	}

	public boolean isTokenExpired(String token) {
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody();
		Date expiration = claims.getExpiration();
		return expiration.before(new Date());
	}

}
