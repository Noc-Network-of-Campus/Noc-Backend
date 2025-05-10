package com.mycompany.myapp.web.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PostRequestDto {

    /**
     * 게시글 수정 요청 DTO
     * - 제목, 카테고리, 본문 내용 수정 가능
     */
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

    /**
     * 게시글 생성 요청 DTO
     * - 제목, 카테고리, 본문, 위치 정보 포함
     */
    @Getter @Setter
    public static class CreatePostRequest {

        @ApiParam(name = "Title", value = "게시글 제목 입력")
        @ApiModelProperty(example = "제목")
        @NotBlank(message = "제목은 필수입니다.")
        private String title;

        @ApiParam(name = "Content", value = "게시글 내용 입력")
        @ApiModelProperty(example = "게시글 내용입니다!")
        @NotBlank(message = "내용은 필수입니다.")
        private String content;

        /** 게시글 카테고리 (ENUM name 형식: 예 - FOOD)
         * null 인 경우 FREE
         */
        @ApiParam(name = "Category", value = "카테고리 입력")
        @ApiModelProperty(example = "FOOD")
        private String category;

        @ApiParam(name = "latitude", value = "사용자 위치의 위도 입력")
        @ApiModelProperty(example = "37.23976")
        @NotNull(message = "위도는 필수입니다.")
        @Range(min = -90, max = 90, message = "위도는 -90~90 사이여야 합니다.")
        private Double latitude;

        @ApiParam(name = "longitude", value = "사용자 위치의 경도 입력")
        @ApiModelProperty(example = "127.08334")
        @NotNull(message = "경도는 필수입니다.")
        @Range(min = -180, max = 180, message = "경도는 -180~180 사이여야 합니다.")
        private Double longitude;
    }
}
