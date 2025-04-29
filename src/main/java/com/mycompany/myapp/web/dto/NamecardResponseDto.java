package com.mycompany.myapp.web.dto;

import lombok.*;

import java.time.LocalDateTime;

public class NamecardResponseDto {
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class OCRResponseDto {
        private String name;
        private String mobile;
        private String email;
        private String company;
        private String department;
        private String position;
        private String tel;
        private String fax;
        private String homepage;
        private String address;
    }
}
