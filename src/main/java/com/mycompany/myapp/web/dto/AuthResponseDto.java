package com.mycompany.myapp.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthResponseDto {
	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ReissueDto {
		private String accessToken;
		private String refreshToken;
	}
}
