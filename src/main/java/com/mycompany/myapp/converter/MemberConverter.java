package com.mycompany.myapp.converter;

import org.springframework.stereotype.Component;

import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.web.dto.MemberResponseDto;

@Component
public class MemberConverter {
	public Member toCreateMember(String email) {
		return Member.builder()
			.email(email)
			.isRegistered(false)
			.build();
	}

	public MemberResponseDto.ProfileDto toProfileDto(Member member) {
		return MemberResponseDto.ProfileDto.builder()
			.nickname(member.getNickname())
			.email(member.getEmail())
			.gender(member.getGender())
			.isRegistered(member.isRegistered())
			.build();
	}
}
