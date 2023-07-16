package com.vaskka.diary.core.utils;


import com.vaskka.diary.core.constant.ResultCodeEnum;
import com.vaskka.diary.core.model.viewobject.CommonResponse;

import java.util.function.Supplier;

public class ResultCodeUtil {
    public static <T extends CommonResponse<R>, R> T buildCommonResponse(Supplier<T> responseSupplier, R data, ResultCodeEnum resultCodeEnum) {
        T response = responseSupplier.get();
        response.setData(data);
        response.setResultCode(resultCodeEnum.name());
        response.setResultMsg(resultCodeEnum.getResultMsg());
        return response;
    }
}
