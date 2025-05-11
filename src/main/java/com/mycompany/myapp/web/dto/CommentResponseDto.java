package com.mycompany.myapp.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class CommentResponseDto {

    /**
     * 댓글 상세 정보를 담는 DTO
     * - 게시글 상세 조회 시 댓글 목록에 사용
     * - 대댓글 포함 여부는 parentCommentId로 구분
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentDetailDto {
        private Long commentId;
        private String nickname;

        /** 댓글 내용 (삭제된 댓글일 경우 '삭제된 댓글입니다.') */
        private String content;

        /** 부모 댓글 ID (null이면 일반 댓글, 값이 있으면 대댓글) */
        private Long parentCommentId;

        /** 현재 로그인한 사용자가 작성한 댓글인지 여부 */
        private Boolean isMyComment;

        /** 현재 로그인한 사용자가 좋아요를 누른 상태인지 여부 */
        private Boolean isLiked;

        private Integer likeCount;

        /** 삭제 여부 (soft delete 처리됨) */
        private Boolean isDeleted;

        private LocalDateTime createdAt;
    }
}
