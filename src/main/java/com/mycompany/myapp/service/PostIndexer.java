package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Post;
import com.mycompany.myapp.domain.PostDocument;
import com.mycompany.myapp.repository.PostRepository;
import com.mycompany.myapp.repository.PostSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostIndexer {

    private final PostRepository postRepository;
    private final PostSearchRepository postSearchRepository;

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
