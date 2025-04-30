package com.mycompany.myapp.domain.enums;

public enum Category {
    BOOTH("부스"),
    PUB("주점"),
    LOST_FOUND("분실물"),
    NOTICE("공지"),
    FREE("자유"),
    EVENT("이벤트");
    private final String categoryName;

    Category(String categoryName) {
        this.categoryName = categoryName;
    }

}
