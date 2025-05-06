package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member getByNickname(String nickname);
}
