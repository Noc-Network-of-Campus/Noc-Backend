package com.mycompany.myapp.web.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;

public class NamecardRequestDto {

    @Getter
    public static class OCRRequestDto{
        @ApiModelProperty(example = "base64 encoding image")
        @ApiParam(name = "base64 encoding image", value = "base64 encoding image", required = true)
        private String base64Image;
    }

    @Getter
    public static class CreateCategoryDto{
        @ApiModelProperty(example = "친구")
        @ApiParam(name = "category", value = "카테고리 이름 입력", required = true)
        private String category;
    }
}
