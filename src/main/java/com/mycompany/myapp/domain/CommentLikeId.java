package com.mycompany.myapp.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 댓글 좋아요(CommentLike) 엔티티의 복합키 클래스
 * - member_id + comment_id로 구성된 복합 기본키
 * - 한 사용자가 하나의 댓글에 한 번만 좋아요를 누를 수 있도록 제한
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CommentLikeId implements Serializable {
    /** 좋아요가 눌린 댓글의 ID */
    private Long comment;

    /** 좋아요를 누른 사용자의 ID */
    private Long member;
}