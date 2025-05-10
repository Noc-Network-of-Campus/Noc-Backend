package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.base.BaseEntity;
import com.mycompany.myapp.domain.enums.ReportTargetType;
import lombok.*;

import javax.persistence.*;

/**
 * 사용자가 게시글 또는 댓글을 신고한 내역을 저장하는 엔티티
 * - 신고 대상은 게시글(POST) 또는 댓글(COMMENT)
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_member_id", nullable = false)
    private Member reporter;

    /** 신고 대상 종류 (게시글 or 댓글) */
    @Enumerated(EnumType.STRING)
    private ReportTargetType targetType; // "POST" or "COMMENT"

    /** 신고 대상 ID (게시글 ID 또는 댓글 ID) */
    @Column(nullable = false)
    private Long targetId; // post.id 또는 comment.id

    @Column(nullable = false)
    private String reason;
}