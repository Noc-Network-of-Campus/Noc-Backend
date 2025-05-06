package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.enums.Category;
import com.mycompany.myapp.domain.enums.SortType;
import com.mycompany.myapp.web.dto.PostResponseDto;

import java.util.List;

public interface PostService {
    List<PostResponseDto.SimplePostDto> getPostsByCategory(Category category, SortType sort, Integer page, Integer size);
}
