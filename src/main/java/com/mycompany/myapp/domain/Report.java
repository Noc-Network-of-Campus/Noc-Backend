package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.base.BaseEntity;
import com.mycompany.myapp.domain.enums.ReportTargetType;
import lombok.*;

import javax.persistence.*;

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

    @Enumerated(EnumType.STRING)
    private ReportTargetType targetType; // "POST" or "COMMENT"

    @Column(nullable = false)
    private Long targetId; // post.id 또는 comment.id

    @Column(nullable = false)
    private String reason;
}