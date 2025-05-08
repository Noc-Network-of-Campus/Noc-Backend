package com.mycompany.myapp.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mycompany.myapp.config.security.handler.JwtAuthenticationEntryPoint;
import com.mycompany.myapp.config.security.handler.JwtExpiredHandler;
import com.mycompany.myapp.config.security.handler.JwtInvalidHandler;
import com.mycompany.myapp.config.security.provider.JwtProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	private final JwtProvider jwtProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		if (bypassTokenValidation(request)) {
			filterChain.doFilter(request, response);
			return;
		}
		String token = resolveToken(request);

		try {
			processTokenValidation(token, request);
			filterChain.doFilter(request, response);
		} catch (JwtExpiredHandler | JwtInvalidHandler e) {
			SecurityContextHolder.clearContext();
			jwtAuthenticationEntryPoint.commence(request, response, e);
			return;
		}
	}

	private void processTokenValidation(String token, HttpServletRequest request) {
		if (token == null) {
			throw new JwtInvalidHandler("JWT 토큰이 없습니다.");
		}
		if (!jwtProvider.validateToken(token)) {
			throw new JwtInvalidHandler("유효하지 않은 JWT 토큰입니다.");
		}
		if (jwtProvider.isTokenExpired(token)) {
			throw new JwtExpiredHandler("JWT 토큰이 만료되었습니다.");
		}
		Authentication authentication = jwtProvider.getAuthentication(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private boolean bypassTokenValidation(HttpServletRequest request) {
		String uri = request.getRequestURI();
		return uri.startsWith("/swagger-ui") ||
			uri.startsWith("/v2/api-docs") ||
			uri.startsWith("/v3/api-docs") ||
			uri.startsWith("/swagger-resources") ||
			uri.startsWith("/webjars") ||
			uri.startsWith("/login") ||
			uri.startsWith("/oauth2") ||
			uri.startsWith("/api/auth/reissue");
	}

	private String resolveToken(HttpServletRequest request) {
		String bearer = request.getHeader("Authorization");
		if (bearer != null && bearer.startsWith("Bearer ")) {
			return bearer.substring(7);
		}
		return null;
	}
}
