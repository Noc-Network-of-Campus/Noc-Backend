package com.mycompany.myapp.web.dto;

import com.mycompany.myapp.domain.Post;
import com.mycompany.myapp.domain.enums.Category;
import lombok.*;

import java.time.LocalDateTime;

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

}
