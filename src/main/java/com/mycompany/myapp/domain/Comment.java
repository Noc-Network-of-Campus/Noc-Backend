package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * 게시글에 대한 댓글(Comment)을 나타내는 엔티티
 * - 대댓글 구조(자기참조)
 * - 좋아요 수 관리
 * - soft delete 지원
 */
@Entity
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    /** 부모 댓글 (대댓글일 경우 설정), null 허용 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment; // nullable 허용

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentLike> commentLikes;

    /** 삭제 여부 (soft delete) */
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    private String content;
    private Integer likeCount;

    public void increaseLikeCount() {
        this.likeCount = (this.likeCount == null) ? 1 : this.likeCount + 1;
    }

    public void decreaseLikeCount() {
        this.likeCount = (this.likeCount == null || this.likeCount <= 0) ? 0 : this.likeCount - 1;
    }

    /**
     * 댓글을 삭제 상태로 변경합니다. (실제 DB 삭제는 아님)
     */
    public void setIsDeleted(){
        this.isDeleted = true;
    }
}
