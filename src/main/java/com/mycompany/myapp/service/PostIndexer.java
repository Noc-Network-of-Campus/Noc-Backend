package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Post;
import com.mycompany.myapp.domain.PostDocument;
import com.mycompany.myapp.repository.PostRepository;
import com.mycompany.myapp.repository.PostSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 게시글(Post) 데이터를 Elasticsearch에 인덱싱(migrate)하는 유틸성 서비스
 * - 주로 초기 데이터 마이그레이션, 테스트용 수동 동기화 등에 사용
 * - Post -> PostDocument 변환 후 저장
 */
@Service
@RequiredArgsConstructor
public class PostIndexer {

    private final PostRepository postRepository;
    private final PostSearchRepository postSearchRepository;

    /**
     * DB에 저장된 모든 게시글을 Elasticsearch로 마이그레이션
     * - 현재는 title 필드만 인덱싱
     * - PostDocument 구조에 따라 추가 필드 확장 가능
     */
    public void migrateToElasticsearch() {
        List<Post> posts = postRepository.findAll();
        List<PostDocument> docs = posts.stream()
                .map(post -> PostDocument.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .build())
                .toList();
        postSearchRepository.saveAll(docs);
    }
}
