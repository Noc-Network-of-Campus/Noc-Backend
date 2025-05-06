package com.mycompany.myapp.web.dto;

import com.mycompany.myapp.domain.Post;
import com.mycompany.myapp.domain.enums.Category;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SimplePostDto {
        private Long postId;
        private Category category;
        private String title;
        private String content;     // 본문 미리보기 (앞 20자)
        private int likeCount;
        private int commentCount;
        private String thumbnailImageUrl;     // sortOrder = 1 인 이미지 URL
        private LocalDateTime createdAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostDetailDto{
        private Long postId;
        private String title;
        private String content;
        private Integer likeCount;
        private Integer commentCount;
        private String category;
        private List<String> images;
        private Boolean isMyPost;
        private List<CommentResponseDto.CommentDetailDto> comments;
        private LocalDateTime createdAt;
    }
}
