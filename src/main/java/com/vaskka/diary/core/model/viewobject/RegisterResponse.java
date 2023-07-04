package com.vaskka.diary.core.model.viewobject;

import com.vaskka.diary.core.model.bizobject.User;
import lombok.Data;

@Data
public class RegisterResponse extends CommonResponse {

    private User user;

}
