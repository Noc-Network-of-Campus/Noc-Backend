package com.mycompany.myapp.web.dto;

import com.mycompany.myapp.domain.enums.Gender;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;

public class MemberRequestDto {
	@Getter
	public static class CreateProfileDto {
		@ApiParam(name = "닉네임", value = "사용자 닉네임 입력", required = true)
		@ApiModelProperty(example = "빛나는 쿠옹")
		private String nickname;

		@ApiParam(name="성별", value="사용자 성별 입력", required = true)
		@ApiModelProperty(example = "MALE" )
		private Gender gender;
	}
}
