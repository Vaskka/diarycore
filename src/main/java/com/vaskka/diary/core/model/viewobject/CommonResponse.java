package com.vaskka.diary.core.model.viewobject;

import lombok.Data;

@Data
public class CommonResponse<T> {

    private String resultCode;

    private String resultMsg;

    private T data;

}
