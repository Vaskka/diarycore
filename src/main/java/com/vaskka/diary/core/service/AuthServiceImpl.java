package com.vaskka.diary.core.service;

import com.google.common.base.Strings;
import com.vaskka.diary.core.facade.AuthFacade;
import com.vaskka.diary.core.model.dataobject.AuthStorageDO;
import com.vaskka.diary.core.utils.CommonUtil;
import com.vaskka.diary.core.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service("authServiceImpl")
public class AuthServiceImpl implements AuthFacade {

//    @Resource
//    private RedisTemplate<String, AuthStorageDO> authRedisTemplate;

    @Override
    public AuthStorageDO localAuthCheck(String userId, String authToken) {
        return new AuthStorageDO();
//        if (Strings.isNullOrEmpty(userId) || Strings.isNullOrEmpty(authToken)) {
//            LogUtil.errorf(log, "[authCheck],auth fail, empty userId or authToken");
//            return null;
//        }
//
//        var cacheStorage = authRedisTemplate.opsForValue().get(getRedisKey(authToken));
//        if (cacheStorage == null) {
//            return null;
//        }
//
//        String localUserId = cacheStorage.getUserId();
//        if (!userId.equals(localUserId)) {
//            LogUtil.errorf(log, "[authCheck],local userId not equal,local:{},userId:{}", localUserId, userId);
//            return null;
//        }
//        return cacheStorage;
    }

    @Override
    public AuthStorageDO localLogin(String userId) {
        return new AuthStorageDO();
//        AuthStorageDO authStorageDO = new AuthStorageDO();
//        authStorageDO.setUserId(userId);
//        authStorageDO.setAuthToken(CommonUtil.getRandom32LengthStr());
//
//        authRedisTemplate.opsForValue().set(getRedisKey(authStorageDO.getAuthToken()), authStorageDO);
//        return authStorageDO;
    }

    private String getRedisKey(String authToken) {
        return "DIARY_AUTH_TOKEN_" + authToken;
    }

}
