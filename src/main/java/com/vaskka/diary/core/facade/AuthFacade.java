package com.vaskka.diary.core.facade;

import com.vaskka.diary.core.model.dataobject.AuthStorageDO;

public interface AuthFacade {

    AuthStorageDO localAuthCheck(String userId, String authToken);

    AuthStorageDO localLogin(String userId);
}
