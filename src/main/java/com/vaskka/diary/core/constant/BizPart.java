package com.vaskka.diary.core.constant;

import lombok.Getter;

public enum BizPart {
    USER(0),

    AUTHOR(1),

    DIARY(2),

    ;

    @Getter
    private final int bizCode;

    BizPart(int bizCode) {
        this.bizCode = bizCode;
    }
}
