package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	//회원탈퇴 할 때 memberId를 dummy 사용자로 설정
	@Modifying
	@Query("UPDATE Comment c SET c.member.id = :dummyMemberId WHERE c.member.id = :targetMemberId")
	void replaceMemberWithDummy(@Param("targetMemberId") Long targetMemberId,
		@Param("dummyMemberId") Long dummyMemberId);
}
