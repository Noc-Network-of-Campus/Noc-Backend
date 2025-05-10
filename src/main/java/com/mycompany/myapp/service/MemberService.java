package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Member;

public interface MemberService {
    Member getCurrentMember();
    String createNickname();
}
