package com.mycompany.myapp.web.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

public class CommentRequestDto {

    @Getter
    public static class CreateCommentDto{
        @ApiParam(name = "Parent Comment ID", value = "부모 댓글 ID 입력")
        @ApiModelProperty(example = "1")
        private Long parentCommentId;

        @ApiParam(name = "Content", value = "댓글 내용 입력", required = true)
        @ApiModelProperty(example = "댓글 내용입니다.")
        private String content;
    }
}
