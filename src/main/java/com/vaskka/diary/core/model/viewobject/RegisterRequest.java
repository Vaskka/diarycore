package com.vaskka.diary.core.model.viewobject;

import lombok.Data;

@Data
public class RegisterRequest {

    private String userName;

    private String avatarUrl;

    private String psw;

}
