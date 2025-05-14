package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.web.dto.MemberRequestDto;
import com.mycompany.myapp.web.dto.MemberResponseDto;

public interface MemberService {

    /**
     * 현재 로그인한 사용자의 Member 정보 반환
     * - Spring Security의 SecurityContext를 기반으로 조회
     *
     * @return 현재 인증된 사용자
     */
    Member getCurrentMember();
    String createNickname();
    void createProfile(Member member, MemberRequestDto.CreateProfileDto createProfileDto);
    MemberResponseDto.ProfileDto getProfile(Member member);

}
