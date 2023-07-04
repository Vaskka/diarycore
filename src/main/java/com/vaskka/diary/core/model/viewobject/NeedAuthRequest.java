package com.vaskka.diary.core.model.viewobject;

import lombok.Data;

@Data
public class NeedAuthRequest {

    private String userId;

    private String authToken;

}
