package com.mycompany.myapp.repository;

import java.util.Optional;

import com.mycompany.myapp.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member getByNickname(String nickname);
    Optional<Member> findMemberByEmail(String email);
    Boolean existsByNickname(String nickname);
    Boolean existsByEmail(String email);
}
