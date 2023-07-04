package com.vaskka.diary.core.controller;

import com.vaskka.diary.core.exceptions.AuthException;
import com.vaskka.diary.core.facade.AuthFacade;
import com.vaskka.diary.core.model.dataobject.AuthStorageDO;
import com.vaskka.diary.core.model.viewobject.NeedAuthRequest;
import com.vaskka.diary.core.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;

public abstract class NeedAuthController {

    @Value("${auth.enable:true}")
    private Boolean authEnable;

    @Resource(name = "authServiceImpl")
    private AuthFacade authServiceImpl;

    protected AuthStorageDO auth(NeedAuthRequest needAuthRequest) {
        if (authEnable) {
            var authObj = authServiceImpl.localAuthCheck(needAuthRequest.getUserId(), needAuthRequest.getAuthToken());
            if (authObj == null) {
                throw new AuthException();
            }

            return authObj;
        } else {
            var authObject = new AuthStorageDO();
            authObject.setAuthToken(CommonUtil.getRandom32LengthStr());
            authObject.setUserId("MOCK_USER_ID_" + CommonUtil.getRandom32LengthStr());
            return authObject;
        }

    }



}
