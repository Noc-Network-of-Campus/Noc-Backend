package com.mycompany.myapp.service.Impl;

import com.mycompany.myapp.config.security.CustomUserDetails;
import com.mycompany.myapp.converter.MemberConverter;
import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.repository.MemberRepository;
import com.mycompany.myapp.service.MemberService;
import com.mycompany.myapp.web.dto.MemberRequestDto;
import com.mycompany.myapp.web.dto.MemberResponseDto;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberConverter memberConverter;

    @Override
    public Member getCurrentMember(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("로그인 되지 않았습니다.");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long memberId = userDetails.getId();

        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    @Override
    public String createNickname() {
        String nickname = generateRandomNickname();
        while (memberRepository.existsByNickname(nickname)) {
            nickname = generateRandomNickname();
        }
        return nickname;
    }

    @Override
    @Transactional
    public void createProfile(Member member, MemberRequestDto.CreateProfileDto request) {
        if(memberRepository.existsByNickname(request.getNickname())){
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }
        member.setNickname(request.getNickname());
        member.setGender(request.getGender());
        member.completeRegistration();
    }

    @Override
    public MemberResponseDto.ProfileDto getProfile(Member member) {
        Member findMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
        return memberConverter.toProfileDto(findMember);
    }

    private static final List<String> ADJECTIVES = List.of(
        "푸른", "빛나는", "조용한", "활기찬", "깊은", "자유로운", "신비한", "따뜻한", "열정적인", "차분한"
    );

    private static final List<String> NOUNS = List.of("쿠옹", "쿠밍");

    private static String generateRandomNickname() {
        String adjective = ADJECTIVES.get(new Random().nextInt(ADJECTIVES.size()));
        String noun = NOUNS.get(new Random().nextInt(NOUNS.size()));
        return adjective + " " + noun;
    }
}
