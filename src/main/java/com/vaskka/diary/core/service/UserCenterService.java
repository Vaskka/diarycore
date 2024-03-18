package com.vaskka.diary.core.service;

import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.log.Log;
import com.vaskka.diary.core.constant.BizPart;
import com.vaskka.diary.core.constant.UserType;
import com.vaskka.diary.core.dal.UserIpMapper;
import com.vaskka.diary.core.dal.UserMapper;
import com.vaskka.diary.core.dal.UserSessionMapper;
import com.vaskka.diary.core.facade.UserCenterFacade;
import com.vaskka.diary.core.model.bizobject.User;
import com.vaskka.diary.core.model.dataobject.UserDO;
import com.vaskka.diary.core.model.dataobject.UserIpDO;
import com.vaskka.diary.core.model.dataobject.UserSessionDO;
import com.vaskka.diary.core.utils.LogUtil;
import com.vaskka.diary.core.utils.uid.UnionIdProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service("userCenterService")
public class UserCenterService implements UserCenterFacade {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserIpMapper userIpMapper;

    @Resource
    private UserSessionMapper userSessionMapper;

    @Resource
    private UnionIdProcessor globalUidComponent;

    // 14天
    private static final long SESSION_VALID_MILLI = 14 * 24 * 60 * 60 * 1000;

    @Override
    @Transactional
    public User registerAdmin(String userName, String psw) {
        UserDO userDO = new UserDO();
        userDO.setUserType(UserType.ADMIN.name());
        userDO.setUserName(userName);
        userDO.setId(globalUidComponent.getUnionId(BizPart.USER));
        userDO.setPsw(getDigestPsw(psw));
        userDO.setExternParam("{}");
        userMapper.insertUser(userDO);
        return buildFromDO(userDO);
    }

    @Override
    @Transactional
    public User registerNormal(String userName, List<String> ips) {
        long uid = globalUidComponent.getUnionId(BizPart.USER);
        UserDO userDO = new UserDO();
        userDO.setUserType(UserType.NORMAL.name());
        userDO.setUserName(userName);
        userDO.setId(uid);
        userDO.setExternParam("{}");
        userMapper.insertUser(userDO);

        // 关联ip
        for (String ip : ips) {
            UserIpDO userIpDO = new UserIpDO();
            userIpDO.setRefUserId(uid);
            userIpDO.setIp(ip);
            userIpMapper.insertUserIp(userIpDO);
        }

        return buildFromDO(userDO);
    }

    @Override
    @Transactional
    public User loginAdmin(String userName, String psw) {
        UserDO userDO = userMapper.findByUserName(userName);
        if (userDO == null) {
            LogUtil.infof(log, "[login],userName={} not exist", userName);
            return null;
        }

        if (userDO.getPsw() == null) {
            // create session
            String authToken = createSession(userDO);
            User user = buildFromDO(userDO);
            user.getExternParam().put("authToken", authToken);
            return user;
        }

        if (userDO.getPsw().equals(getDigestPsw(psw))) {
            // create session
            String authToken = createSession(userDO);
            User user = buildFromDO(userDO);
            user.getExternParam().put("authToken", authToken);
            return user;
        } else {
            LogUtil.infof(log, "[login],userName={} psw not equals, input={}", userName, psw);
            return null;
        }
    }

    @Override
    public User getUserProfile(String userId) {
        return buildFromDO(userMapper.findById(Long.parseLong(userId)));
    }

    @Override
    public User getUserByAuthToken(String authToken) {
        if (StringUtils.isNotEmpty(authToken)) {
            List<UserSessionDO> sessionDOList = userSessionMapper.findByAuthTokenAndExpireTimestampGte(authToken, System.currentTimeMillis());
            if (sessionDOList != null && !sessionDOList.isEmpty()) {
                UserSessionDO sessionDO = sessionDOList.get(0);
                return buildFromDO(userMapper.findById(sessionDO.getUserId()));
            }
        }

        // not found
        return null;
    }

    @Override
    public User getUserByIp(String ip) {
        if (StringUtils.isEmpty(ip)) {
            return null;
        }

        UserIpDO userIpDO = userIpMapper.findByIp(ip);
        if (userIpDO == null) {
            LogUtil.infof(log, "ip:{},not reg", ip);
            return null;
        }

        return buildFromDO(userMapper.findById(userIpDO.getRefUserId()));
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

    private String getDigestPsw(String psw) {
        return DigestUtils.md5Hex(psw);
    }

    private String createSession(UserDO userDO) {
        String authToken = DigestUtils.md5Hex(UUID.randomUUID().toString());
        UserSessionDO userSessionDO = new UserSessionDO();
        userSessionDO.setAuthToken(authToken);
        userSessionDO.setUserId(userDO.getId());
        userSessionDO.setExpireTimestamp(System.currentTimeMillis() + SESSION_VALID_MILLI);
        Integer eff = userSessionMapper.insertUserIp(userSessionDO);
        LogUtil.infof(log, "[login],create {} session,session={}", eff, userSessionDO);
        return authToken;
    }
}
