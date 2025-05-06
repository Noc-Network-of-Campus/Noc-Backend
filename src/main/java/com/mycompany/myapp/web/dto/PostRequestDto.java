package com.mycompany.myapp.web.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;

public class PostRequestDto {

    @Getter
    public static class UpdatePostDto {
        @ApiParam(name = "Title", value = "게시글 제목 입력")
        @ApiModelProperty(example = "수정된 제목")
        private String title;

        @ApiParam(name = "Category", value = "카테고리 입력")
        @ApiModelProperty(example = "FOOD")
        private String category;

        @ApiParam(name = "Content", value = "게시글 내용 입력")
        @ApiModelProperty(example = "수정된 게시글 내용입니다!")
        private String content;
    }
}
