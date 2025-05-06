package com.mycompany.myapp.service.Impl;

import com.mycompany.myapp.converter.PostConverter;
import com.mycompany.myapp.domain.enums.Category;
import com.mycompany.myapp.domain.enums.SortType;
import com.mycompany.myapp.repository.PostRepository;
import com.mycompany.myapp.service.PostService;
import com.mycompany.myapp.web.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostConverter postConverter;

    // 카테고리 + 정렬 기준에 따라 게시글 목록을 페이징 조회
    @Override
    public List<PostResponseDto.SimplePostDto> getPostsByCategory(Category category, SortType sort, Integer page, Integer size){
        if (page < 1) throw new IllegalArgumentException("페이지는 1부터 시작합니다.");
        if (size < 1) throw new IllegalArgumentException("size는 1 이상이어야 합니다.");

        Pageable pageable;

        // 정렬 기준에 따라 Pageable 생성
        if (sort == SortType.LIKE) {
            pageable = PageRequest.of(page - 1, size,
                    Sort.by(Sort.Direction.DESC, "likeCount")
                            .and(Sort.by(Sort.Direction.DESC, "createdAt")));
        } else {
            pageable = PageRequest.of(page - 1, size,
                    Sort.by(Sort.Direction.DESC, "createdAt"));
        }

        // category가 null이면 전체 조회, 아니면 해당 카테고리만 조회
        return (category == null
                ? postRepository.findAll(pageable)
                : postRepository.findByCategory(category, pageable)
        ).stream()
                .map(postConverter::toSimplePostDto)
                .collect(Collectors.toList());
    }
}
