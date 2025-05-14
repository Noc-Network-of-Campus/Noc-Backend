package com.mycompany.myapp.web.dto;

import com.mycompany.myapp.domain.enums.Gender;

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

	// 사용자 프로필
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ProfileDto {
		private String nickname;
		private String email;
		private Gender gender;
		private boolean isRegistered;
	}
}
