package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PostDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostSearchRepository extends ElasticsearchRepository<PostDocument, Long> {
    // 제목에 키워드가 포함된 게시글 검색
    Page<PostDocument> findByTitleContaining(String keyword, Pageable pageable);
    Page<PostDocument> findByTitleContainingAndIdLessThan(String keyword, Long lastPostId, Pageable pageable);
    void deleteById(Long postId);
}
