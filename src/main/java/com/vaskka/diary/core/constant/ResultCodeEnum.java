package com.vaskka.diary.core.constant;

import lombok.Getter;

public enum ResultCodeEnum {

    OK("成功"),

    NEED_LOGIN("需要登录"),

    SYS_ERR("系统异常"),

    ;
    @Getter
    private final String resultMsg;

    ResultCodeEnum(String resultMsg) {
        this.resultMsg = resultMsg;
    }
}
