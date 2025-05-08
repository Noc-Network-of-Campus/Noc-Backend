package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.web.dto.AuthRequestDto;
import com.mycompany.myapp.web.dto.AuthResponseDto;

public interface AuthService {
	void saveRefreshToken(Long memberId, String refreshToken);
	AuthResponseDto.ReissueDto reissue(AuthRequestDto.ReissueDto request);
}
