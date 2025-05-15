package com.mycompany.myapp.service;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DummyMemberInitializer implements ApplicationRunner {
	private final MemberRepository memberRepository;
	@Override
	@Transactional
	public void run(ApplicationArguments args) {
		// 더미 데이터 생성 로직을 여기에 작성합니다.
		String deletedEmail = "deleted@user.com";
		boolean exists = memberRepository.existsByEmail(deletedEmail);

		if (!exists) {
			// 더미 데이터 생성
			Member deleted = Member.builder()
				.email(deletedEmail)
				.nickname("deleted")
				.build();
			memberRepository.save(deleted);
		}
	}
}
