package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Comment;
import com.mycompany.myapp.domain.CommentLike;
import com.mycompany.myapp.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentAndMember(Comment comment, Member member);
}
