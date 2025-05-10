package com.mycompany.myapp.converter;

import com.mycompany.myapp.domain.Comment;
import com.mycompany.myapp.domain.CommentLike;
import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.domain.Post;
import com.mycompany.myapp.web.dto.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommentConverter {

    /**
     * 댓글 작성 요청을 기반으로 Comment 엔티티 생성
     *
     * @param member 댓글 작성자
     * @param post 댓글이 속한 게시글
     * @param parent 부모 댓글 (대댓글인 경우, null 가능)
     * @param content 댓글 내용
     * @return 생성된 Comment 엔티티
     */
    public Comment toCreateComment(Member member, Post post, Comment parent, String content){
        return Comment.builder()
                .post(post)
                .member(member)
                .content(content)
                .parentComment(parent)
                .likeCount(0)
                .isDeleted(false)
                .build();
    }

    /**
     * 댓글 좋아요 클릭 시 사용할 CommentLike 엔티티 생성
     *
     * @param comment 대상 댓글
     * @param member 좋아요 누른 사용자
     * @return CommentLike 엔티티
     */
    public CommentLike toCommentLike(Comment comment, Member member){
        return CommentLike.builder()
                .comment(comment)
                .member(member)
                .build();
    }

    /**
     * Comment 엔티티를 댓글 상세 응답 DTO로 변환
     * - 삭제된 댓글은 '삭제된 댓글입니다.'로 표시
     * - isMyComment, isLiked 여부도 함께 반환
     *
     * @param comment 댓글 엔티티
     * @param member 현재 로그인한 사용자
     * @return 댓글 상세 DTO
     */
    public CommentResponseDto.CommentDetailDto toCommentDetailDto(Comment comment, Member member) {
        boolean isMyComment = comment.getMember().getId().equals(member.getId());
        boolean isLiked = comment.getCommentLikes().stream()
                .anyMatch(like -> like.getMember().getId().equals(member.getId()));
        boolean isDeleted = Boolean.TRUE.equals(comment.getIsDeleted());

        return CommentResponseDto.CommentDetailDto.builder()
                .commentId(comment.getId())
                .nickname(member.getNickname())
                .content(isDeleted ? "삭제된 댓글입니다." : comment.getContent())
                .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                .isMyComment(isMyComment)
                .isLiked(isLiked)
                .likeCount(comment.getLikeCount())
                .isDeleted(isDeleted)
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
