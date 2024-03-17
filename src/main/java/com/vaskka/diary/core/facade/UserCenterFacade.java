package com.vaskka.diary.core.facade;

import com.vaskka.diary.core.model.bizobject.User;

import java.util.List;

public interface UserCenterFacade {

    User registerAdmin(String userName, String psw);

    User registerNormal(String userName, String ips);

    User getUserProfile(String userId);

    Integer updateUserIps(String userId, List<String> ips);

    User updateUserName(String userId, String userName);
}
