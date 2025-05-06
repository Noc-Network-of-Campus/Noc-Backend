package com.mycompany.myapp.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.myapp.converter.MemberConverter;
import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	private final MemberRepository memberRepository;
	private final MemberConverter memberConverter;

	@Override
	@Transactional
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		String userNameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails()
			.getUserInfoEndpoint()
			.getUserNameAttributeName();

		String email = (String) oAuth2User.getAttributes().get("email");
		//경희대 이메일 확인
		if (!email.endsWith("@khu.ac.kr")) {
			throw new IllegalArgumentException("경희대 이메일만 로그인 가능합니다.");
		}

		Optional<Member> memberByEmail = memberRepository.findMemberByEmail(email);
		Member member;
		if(memberByEmail.isEmpty()) {
			// 회원가입 처리
			member = memberConverter.toCreateMember(email);
			memberRepository.save(member);
		} else {
			member = memberByEmail.get();
		}

		Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
		attributes.put("memberId", member.getId());
		attributes.put("isRegistered", member.getIsRegistered());

		return new DefaultOAuth2User(
			Collections.singleton(new SimpleGrantedAuthority("USER")),
			attributes,
			userNameAttributeName
		);

	}

}
