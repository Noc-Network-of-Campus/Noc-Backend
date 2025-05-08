package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.domain.enums.Category;
import com.mycompany.myapp.domain.enums.LikeResult;
import com.mycompany.myapp.domain.enums.SortType;
import com.mycompany.myapp.web.dto.PostRequestDto;
import com.mycompany.myapp.web.dto.PostResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    List<PostResponseDto.SimplePostDto> getPostsByCategoryWithCursor(Category category, SortType sort, Long lastPostId, Integer size);
    List<PostResponseDto.SimplePostDto> getPostsByCategoryWithOffest(Category category, SortType sort, Integer page, Integer size);
    void deletePost(Long postId, Member member);
    PostResponseDto.PostDetailDto getPostDetail(Long postId, Member member);
    LikeResult togglePostLike(Long postId, Member member);
    void updatePost(Long postId, Member member, PostRequestDto.UpdatePostDto request);
    void createPost(PostRequestDto.CreatePostRequest request, List<MultipartFile> images, Member member);
    List<PostResponseDto.SimplePostDto> searchByTitle(String keyword, Integer page, Integer size);
}
