package com.mycompany.myapp.domain.enums;

public enum Category {
    EXPERIENCE("체험부스"),
    FOOD("먹거리부스"),
    MARKET("플리마켓"),
    LOST("분실물"),
    MATCHING("인연찾기"),
    FREE("사담"),
    NOTICE("공지");
    private final String categoryName;

    Category(String categoryName) {
        this.categoryName = categoryName;
    }
}
