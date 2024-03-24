package com.vaskka.diary.core.model.viewobject;

import com.vaskka.diary.core.model.bizobject.User;
import lombok.Data;

@Data
public class NeedAuthRequest {

    private String userId;

    private String authToken;

    private User user;
}
