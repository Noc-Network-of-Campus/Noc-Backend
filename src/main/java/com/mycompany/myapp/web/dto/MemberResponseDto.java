package com.mycompany.myapp.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberResponseDto {
	// 사용자 닉네임
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class NicknameDto {
		private String nickname;
	}
}
