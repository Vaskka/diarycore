package com.vaskka.diary.core.service;

import com.alibaba.fastjson.JSONObject;
import com.vaskka.diary.core.constant.BizPart;
import com.vaskka.diary.core.constant.UserType;
import com.vaskka.diary.core.dal.UserIpMapper;
import com.vaskka.diary.core.dal.UserMapper;
import com.vaskka.diary.core.facade.UserCenterFacade;
import com.vaskka.diary.core.model.bizobject.User;
import com.vaskka.diary.core.model.dataobject.UserDO;
import com.vaskka.diary.core.model.dataobject.UserIpDO;
import com.vaskka.diary.core.utils.LogUtil;
import com.vaskka.diary.core.utils.uid.UnionIdProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service("userCenterService")
public class UserCenterService implements UserCenterFacade {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserIpMapper userIpMapper;

    @Resource
    private UnionIdProcessor globalUidComponent;

    @Override
    @Transactional
    public User registerAdmin(String userName, String psw) {
        UserDO userDO = new UserDO();
        userDO.setUserType(UserType.ADMIN.name());
        userDO.setUserName(userName);
        userDO.setId(globalUidComponent.getUnionId(BizPart.USER));
        userDO.setPsw(psw);
        userDO.setExternParam("{}");
        userMapper.insertUser(userDO);
        return buildFromDO(userDO);
    }

    @Override
    @Transactional
    public User registerNormal(String userName, String ips) {
        UserDO userDO = new UserDO();
        userDO.setUserType(UserType.NORMAL.name());
        userDO.setUserName(userName);
        userDO.setId(globalUidComponent.getUnionId(BizPart.USER));
        userDO.setIps(ips);
        userDO.setExternParam("{}");
        userMapper.insertUser(userDO);
        return buildFromDO(userDO);
    }

    @Override
    public User getUserProfile(String userId) {
        return buildFromDO(userMapper.findById(Long.parseLong(userId)));
    }

    @Override
    @Transactional
    public Integer updateUserIps(String userId, List<String> ips) {
        var userIpList = userIpMapper.findByRefUserId(Long.valueOf(userId));
        for (var userIpDO : userIpList) {
            userIpMapper.deleteByIp(userIpDO.getIp());
        }

        int count = 0;
        for (String ip : ips) {
            UserIpDO userIpDO = new UserIpDO();
            userIpDO.setRefUserId(Long.valueOf(userId));
            userIpDO.setIp(ip);
            count += userIpMapper.insertUserIp(userIpDO);
        }
        return count;
    }

    @Override
    @Transactional
    public User updateUserName(String userId, String userName) {
        Integer effect = userMapper.updateUserName(Long.valueOf(userId), userName);
        LogUtil.infof(log, "[userCenter],updateUserName,userId={},userName={},eff={}", userId, userName, effect);
        return buildFromDO(userMapper.findById(Long.valueOf(userId)));
    }

    private User buildFromDO(UserDO userDO) {
        User user = new User();
        user.setUid(String.valueOf(userDO.getId()));
        user.setUserType(userDO.getUserType());
        user.setUserName(userDO.getUserName());
        user.setExternParam(JSONObject.parseObject(userDO.getExternParam()));
        return user;
    }
}
