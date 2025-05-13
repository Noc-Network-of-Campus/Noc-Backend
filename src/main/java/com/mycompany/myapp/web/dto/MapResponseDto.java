package com.mycompany.myapp.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MapResponseDto {

    /**
     * 지도에 표시할 단일 핀 정보를 담는 DTO
     * - 마커 ID, 카테고리, 좌표 정보를 포함
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MapPinDto {
        private Long postId;
        private String category;
        private Double latitude;
        private Double longitude;
    }

}
