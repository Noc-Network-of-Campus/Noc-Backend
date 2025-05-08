package com.mycompany.myapp.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthRequestDto {
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ReissueDto {
		private String accessToken;
		private String refreshToken;
	}
}
