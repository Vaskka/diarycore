package com.vaskka.diary.core.utils;


import com.vaskka.diary.core.constant.ResultCodeEnum;
import com.vaskka.diary.core.model.viewobject.CommonResponse;

import java.util.function.Supplier;

public class ResultCodeUtil {
    public static <T extends CommonResponse> T buildCommonResponse(Supplier<T> responseSupplier, ResultCodeEnum resultCodeEnum) {
        T response = responseSupplier.get();

        response.setResultCode(resultCodeEnum.name());
        response.setResultMsg(resultCodeEnum.getResultMsg());
        return response;
    }
}
