package com.vaskka.diary.core.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.vaskka.diary.core.constant.BizPart;
import com.vaskka.diary.core.dal.UserMapper;
import com.vaskka.diary.core.exceptions.UserCenterException;
import com.vaskka.diary.core.exceptions.UserInfoUpdateException;
import com.vaskka.diary.core.facade.UserCenterFacade;
import com.vaskka.diary.core.model.bizobject.User;
import com.vaskka.diary.core.model.dataobject.UserDO;
import com.vaskka.diary.core.utils.LogUtil;
import com.vaskka.diary.core.utils.uid.UnionIdProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Slf4j
@Service("userCenterService")
public class UserCenterService implements UserCenterFacade {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UnionIdProcessor globalUidComponent;

    @Override
    public User register(String userName, String avatarUrl, String psw) {

        var now = LocalDateTime.now();
        UserDO userDO = new UserDO();
        userDO.setGmtCreate(now);
        userDO.setGmtModified(now);
        userDO.setUserName(userName);
        userDO.setAvatarUrl(avatarUrl);
        userDO.setPsw(psw);
        userDO.setId(globalUidComponent.getUnionId(BizPart.USER));
        userDO.setExternParam(new JSONObject().toJSONString());
        userMapper.insertUser(userDO);
        LogUtil.infof(log, "[userCenter],register,success register userName:{}", userName);
        return buildUserFromDO(userDO);
    }

    @Override
    public User getUserProfile(String userId) {
        UserDO userDO = userMapper.findById(Long.parseLong(userId));
        if (userDO == null) {
            LogUtil.errorf(log, "[userCenter],get profile err,uid:{}", userId);
            throw new UserCenterException("user not exist error");
        }

        return buildUserFromDO(userDO);
    }

    @Override
    public User updateUserInfo(String userId, String userName, String avatarUrl) {
        if (Strings.isNullOrEmpty(userName) || Strings.isNullOrEmpty(avatarUrl)) {
            throw new UserInfoUpdateException();
        }

        Integer effectRow = userMapper.updateUserInfo(Long.parseLong(userId), userName, avatarUrl);
        if (effectRow == null || effectRow == 0) {
            LogUtil.errorf(log, "[userCenter],updateUserInfo error");
            throw new UserInfoUpdateException();
        }
        LogUtil.infof(log, "[updateUserInfo],{},userName={},avatarUrl={}", userId, userName, avatarUrl);
        return getUserProfile(userId);
    }

    /**
     * DO -> BO
     * @param userDO user data object
     * @return User
     */
    private User buildUserFromDO(UserDO userDO) {
        User user = new User();
        user.setUid(userDO.getId().toString());
        user.setUserName(userDO.getUserName());
        user.setAvatarUrl(userDO.getAvatarUrl());
        user.setExternParam(JSONObject.parseObject(userDO.getExternParam()));
        return user;
    }
}
