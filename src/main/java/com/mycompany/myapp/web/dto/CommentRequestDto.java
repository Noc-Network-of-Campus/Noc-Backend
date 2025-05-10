package com.mycompany.myapp.web.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

public class CommentRequestDto {

    /**
     * 댓글 작성 요청에 사용되는 DTO
     * - 일반 댓글 또는 대댓글을 작성할 때 사용
     */
    @Getter
    public static class CreateCommentDto{

        /**
         * 대댓글일 경우, 부모 댓글의 ID
         * - null이면 일반 댓글로 처리됨
         */
        @ApiParam(name = "Parent Comment ID", value = "부모 댓글 ID 입력")
        @ApiModelProperty(example = "1")
        private Long parentCommentId;

        /**
         * 댓글 본문 내용
         * - 1자 이상 필수
         */
        @ApiParam(name = "Content", value = "댓글 내용 입력", required = true)
        @ApiModelProperty(example = "댓글 내용입니다.")
        @NotBlank(message = "내용은 필수입니다.")
        private String content;
    }
}
