package com.mycompany.myapp.web.dto;

import com.mycompany.myapp.domain.enums.Category;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDto {

    /**
     * 게시글 목록 조회 시 사용하는 간단한 게시글 응답 DTO
     * - 썸네일 이미지, 카운트 정보, 작성일 등 포함
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SimplePostDto {
        private Long postId;
        private Category category;
        private String title;

        /** 게시글 본문 미리보기 (앞 20~50자) */
        private String content;

        private int likeCount;
        private int commentCount;

        /** 썸네일 이미지 URL (sortOrder == 0) */
        private String thumbnailImageUrl;

        private LocalDateTime createdAt;
    }

    /**
     * 게시글 상세 조회 시 사용하는 응답 DTO
     * - 전체 본문, 이미지 리스트, 댓글 목록, 좋아요 여부 등 포함
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostDetailDto{
        private Long postId;
        private String title;
        private String nickname;
        private String content;
        private Integer likeCount;
        private Integer commentCount;
        private String category;
        private List<String> images;

        /** 현재 로그인한 사용자가 작성한 게시글인지 여부 */
        private Boolean isMyPost;

        /** 현재 로그인한 사용자가 좋아요를 눌렀는지 여부 */
        private Boolean isLiked;

        private List<CommentResponseDto.CommentDetailDto> comments;
        private LocalDateTime createdAt;
    }
}
