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

    public CommentLike toCommentLike(Comment comment, Member member){
        return CommentLike.builder()
                .comment(comment)
                .member(member)
                .build();
    }

    public CommentResponseDto.CommentDetailDto toCommentDetailDto(Comment comment, Member member) {
        boolean isMyComment = comment.getMember().getId().equals(member.getId());
        boolean isLiked = comment.getCommentLikes().stream()
                .anyMatch(like -> like.getMember().getId().equals(member.getId()));
        boolean isDeleted = Boolean.TRUE.equals(comment.getIsDeleted());

        return CommentResponseDto.CommentDetailDto.builder()
                .commentId(comment.getId())
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
