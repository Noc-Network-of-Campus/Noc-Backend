package com.mycompany.myapp.config.security.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.mycompany.myapp.config.security.provider.JwtProvider;
import com.mycompany.myapp.service.AuthService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
	private final JwtProvider jwtProvider;
	private final AuthService authService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication) throws IOException {
		OAuth2User user = (OAuth2User) authentication.getPrincipal();

		Long memberId = (Long) user.getAttributes().get("memberId");
		String email = (String) user.getAttributes().get("email");
		Boolean isRegistered = (Boolean) user.getAttributes().get("isRegistered");

		//Access Token 생성
		String jwt = jwtProvider.generateToken(memberId, email, isRegistered);

		//Refresh Token 생성
		String refreshToken = jwtProvider.generateToken(memberId);
		authService.saveRefreshToken(memberId, refreshToken);

		String redirectUrl = "http://localhost:3000/oauth2/redirect?accessToken=" + jwt + "&refreshToken=" + refreshToken;
		response.sendRedirect(redirectUrl);
	}

}
