package com.mycompany.myapp.config.security.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.mycompany.myapp.config.security.provider.JwtProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
	private final JwtProvider jwtProvider;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication) throws IOException {
		OAuth2User user = (OAuth2User) authentication.getPrincipal();

		//Access Token 생성
		String jwt = jwtProvider.generateToken(
			(Long) user.getAttributes().get("memberId"),
			(String) user.getAttributes().get("email"),
			(Boolean) user.getAttributes().get("isRegistered")
		);

		//Refresh Token 생성
		String refreshToken = jwtProvider.generateToken(
			(Long) user.getAttributes().get("memberId")
		);



		response.setContentType("application/json;charset=UTF-8");
		response
			.getWriter()
			.write("{\"accessToken\": \"" + jwt + "\", \"refreshToken\": \"" + refreshToken + "\"}");
	}
}
