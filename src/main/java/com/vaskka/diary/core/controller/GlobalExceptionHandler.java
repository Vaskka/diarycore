package com.vaskka.diary.core.controller;

import com.vaskka.diary.core.constant.ResultCodeEnum;
import com.vaskka.diary.core.exceptions.AuthException;
import com.vaskka.diary.core.model.viewobject.CommonResponse;
import com.vaskka.diary.core.utils.ResultCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            AuthException.class
    })
    public CommonResponse handleAuthException(AuthException authException) {
        return ResultCodeUtil.buildCommonResponse(CommonResponse::new, ResultCodeEnum.NEED_LOGIN);
    }

}
