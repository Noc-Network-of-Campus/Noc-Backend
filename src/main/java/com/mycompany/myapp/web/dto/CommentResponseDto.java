package com.mycompany.myapp.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class CommentResponseDto {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentDetailDto {
        private Long commentId;
        private String content;
        private Long parentCommentId;
        private Boolean isMyComment;
        private Boolean isLiked;
        private Integer likeCount;
        private Boolean isDeleted;
        private LocalDateTime createdAt;
    }
}
