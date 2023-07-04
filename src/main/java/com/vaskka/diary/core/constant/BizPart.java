package com.vaskka.diary.core.constant;

import lombok.Getter;

public enum BizPart {
    USER(0),

    ORDER(1),

    RECORD(2),

    ;

    @Getter
    private final int bizCode;

    BizPart(int bizCode) {
        this.bizCode = bizCode;
    }
}
