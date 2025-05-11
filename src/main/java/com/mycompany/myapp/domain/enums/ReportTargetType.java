package com.mycompany.myapp.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ReportTargetType {
    POST, COMMENT;

    @JsonCreator
    public static ReportTargetType from(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        return ReportTargetType.valueOf(value.toUpperCase());
    }
}
