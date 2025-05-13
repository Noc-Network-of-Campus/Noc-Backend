package com.mycompany.myapp.service.Impl;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.mycompany.myapp.config.security.provider.JwtProvider;
import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.domain.RefreshToken;
import com.mycompany.myapp.repository.MemberRepository;
import com.mycompany.myapp.repository.RefreshTokenRepository;
import com.mycompany.myapp.service.AuthService;
import com.mycompany.myapp.web.dto.AuthRequestDto;
import com.mycompany.myapp.web.dto.AuthResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final RefreshTokenRepository refreshTokenRepository;
	private final MemberRepository memberRepository;
	private final JwtProvider jwtProvider;


	@Override
	public void saveRefreshToken(Long memberId, String refreshToken) {
		refreshTokenRepository.save(RefreshToken.builder()
			.memberId(memberId)
			.refreshToken(refreshToken)
			.build()
		);
	}

	@Override
	public AuthResponseDto.ReissueDto reissue(AuthRequestDto.ReissueDto request) {
		String accessToken = request.getAccessToken();
		String refreshToken = request.getRefreshToken();
		long memberId = jwtProvider.getMemberId(refreshToken);
		// Refresh Token 검증
		RefreshToken refreshTokenEntity = refreshTokenRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("리프레시 토큰이 만료되었습니다."));
		if (!refreshTokenEntity.getRefreshToken().equals(refreshToken)) {
			// Refresh Token이 DB에 저장된 값과 다를 경우 -> 탈취 위험
			// DB에 저장된 Refresh Token을 삭제
			refreshTokenRepository.delete(refreshTokenEntity);
			throw new IllegalArgumentException("리프레시 토큰이 일치하지 않습니다.");
		}
		// Access Token 검증
		System.out.println("accessToken = " + accessToken);
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
		if(!Objects.equals(jwtProvider.getMemberId(accessToken), memberId)){
			throw new IllegalArgumentException("엑세스 토큰이 일치하지 않습니다.");
		}

		// Access Token 재발급
		String newAccessToken = jwtProvider.generateToken(member.getId(), member.getEmail());
		// Refresh Token 재발급
		String newRefreshToken = jwtProvider.generateToken(member.getId());

		// Refresh Token 저장
		refreshTokenEntity.updateRefreshToken(newRefreshToken);
		refreshTokenRepository.save(refreshTokenEntity);

		return AuthResponseDto.ReissueDto.builder()
			.accessToken(newAccessToken)
			.refreshToken(newRefreshToken)
			.build();
	}
}
