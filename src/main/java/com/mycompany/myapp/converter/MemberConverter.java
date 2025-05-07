package com.mycompany.myapp.converter;

import org.springframework.stereotype.Component;

import com.mycompany.myapp.domain.Member;

@Component
public class MemberConverter {
	public Member toCreateMember(String email) {
		return Member.builder()
			.email(email)
			.isRegistered(false)
			.build();
	}
}
