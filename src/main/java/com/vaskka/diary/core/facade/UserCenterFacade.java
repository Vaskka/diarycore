package com.vaskka.diary.core.facade;

import com.vaskka.diary.core.model.bizobject.User;

public interface UserCenterFacade {

    User register(String userName, String avatarUrl, String psw);

    User getUserProfile(String userId);

    User updateUserInfo(String userId, String userName, String avatarUrl);
}
