package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.base.BaseEntity;
import lombok.*;

import javax.persistence.*;

/**
 * 게시글(Post)에 대한 좋아요를 나타내는 엔티티
 * - 복합 키 (member_id + post_id)를 사용하여 한 사용자가 같은 게시글에 중복 좋아요 방지
 */
@Entity
@IdClass(PostLikeId.class)
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostLike extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @Id
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @Id
    @JoinColumn(name = "post_id")
    private Post post;
}
