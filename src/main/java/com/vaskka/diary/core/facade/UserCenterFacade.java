package com.vaskka.diary.core.facade;

import com.vaskka.diary.core.model.bizobject.User;

import java.util.List;

public interface UserCenterFacade {

    User registerAdmin(String userName, String psw);

    User registerNormal(String userName, List<String> ips);

    User loginAdmin(String userName, String psw);

    User getUserProfile(String userId);

    User getUserByAuthToken(String authToken);

    User getUserByIp(String ip);

    Integer updateUserIps(String userId, List<String> ips);

    User updateUserName(String userId, String userName);
}
