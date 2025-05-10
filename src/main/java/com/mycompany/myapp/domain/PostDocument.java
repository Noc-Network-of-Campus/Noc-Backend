package com.mycompany.myapp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import lombok.*;

/**
 * Elasticsearch에 저장되는 게시글 문서 모델
 * - 검색 대상 필드만 포함
 */
@Document(indexName = "posts")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDocument {

    /** 게시글 ID (Elasticsearch 문서 ID로 사용) */
    @Id
    private Long id;

    private String title;
//    private String content;
//    private String category;
//    private LocalDateTime createdAt;
}
