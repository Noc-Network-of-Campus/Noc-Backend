package com.mycompany.myapp.web.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

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

    @Getter @Setter
    public static class CreatePostRequest {
        @ApiParam(name = "Title", value = "게시글 제목 입력")
        @ApiModelProperty(example = "제목")
        private String title;

        @ApiParam(name = "Content", value = "게시글 내용 입력")
        @ApiModelProperty(example = "게시글 내용입니다!")
        private String content;

        @ApiParam(name = "Category", value = "카테고리 입력")
        @ApiModelProperty(example = "FOOD")
        private String category;

        @ApiParam(name = "latitude", value = "사용자 위치의 위도 입력")
        @ApiModelProperty(example = "37.23976")
        private Double latitude;

        @ApiParam(name = "longitude", value = "사용자 위치의 경도 입력")
        @ApiModelProperty(example = "127.08334")
        private Double longitude;
    }
}
