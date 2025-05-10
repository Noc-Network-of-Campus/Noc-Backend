package com.mycompany.myapp.domain;

import lombok.*;

import javax.persistence.*;

/**
 * 게시글(Post)에 첨부된 이미지 정보를 나타내는 엔티티
 * - 하나의 게시글은 여러 개의 이미지를 가질 수 있음
 * - sortOrder를 통해 이미지 순서를 제어
 */
@Entity
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Post post;

    private String imageUrl;

    /** 이미지 순서 (0부터 시작, 썸네일 지정 등 활용) */
    private Integer sortOrder;
}
