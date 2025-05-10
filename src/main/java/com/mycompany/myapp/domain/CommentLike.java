package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.base.BaseEntity;
import lombok.*;

import javax.persistence.*;

/**
 * 댓글에 대한 좋아요를 나타내는 엔티티
 * - 복합키 (member_id + comment_id) 기반
 * - 한 사용자는 하나의 댓글에 한 번만 좋아요를 누를 수 있음
 */
@Entity
@IdClass(CommentLikeId.class)
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentLike extends BaseEntity {

    /** 좋아요를 누른 사용자 */
    @ManyToOne(fetch = FetchType.LAZY)
    @Id
    @JoinColumn(name = "member_id")
    private Member member;

    /** 좋아요가 눌린 댓글 */
    @ManyToOne(fetch = FetchType.LAZY)
    @Id
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
