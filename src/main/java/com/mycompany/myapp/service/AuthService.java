package com.mycompany.myapp.service;

import org.springframework.stereotype.Service;

import com.mycompany.myapp.domain.RefreshToken;
import com.mycompany.myapp.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final RefreshTokenRepository refreshTokenRepository;

	public void saveRefreshToken(Long memberId, String refreshToken) {
		refreshTokenRepository.save(RefreshToken.builder()
			.memberId(memberId)
			.refreshToken(refreshToken)
			.build()
		);
	}
}
