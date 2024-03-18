package com.vaskka.diary.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.log.Log;
import com.vaskka.diary.core.constant.ResultCodeEnum;
import com.vaskka.diary.core.facade.AuthFacade;
import com.vaskka.diary.core.facade.UserCenterFacade;
import com.vaskka.diary.core.model.bizobject.User;
import com.vaskka.diary.core.model.viewobject.*;
import com.vaskka.diary.core.utils.IpUtil;
import com.vaskka.diary.core.utils.LogUtil;
import com.vaskka.diary.core.utils.ResultCodeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Tag(name = "鉴权接口")
@RequestMapping(value = "/v1/api/user")
@RestController
public class UserController extends NeedAuthController {

    @Resource
    private UserCenterFacade userCenterService;

    @Resource
    private AuthFacade authServiceImpl;

//    @Operation(summary = "注册")
//    @PostMapping(value = "/register")
//    public RegisterResponse register(@RequestBody RegisterRequest request) {
//        var user = userCenterService.registerNormal(request.getUserName(), request.getAvatarUrl(), request.getPsw());
//        return ResultCodeUtil.buildCommonResponse(RegisterResponse::new, user, ResultCodeEnum.OK);
//    }

    @Operation(summary = "登陆")
    @GetMapping(value = "/auth/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        if (loginRequest == null || loginRequest.getUserName() == null) {
            LogUtil.errorf(log, "login invalid request");
            return ResultCodeUtil.buildCommonResponseSimple(new LoginResponse(), null, ResultCodeEnum.PSW_NOT_EQUALS);
        }
        User userWithAuthToken = userCenterService.loginAdmin(loginRequest.getUserName(), loginRequest.getPsw());

        if (userWithAuthToken == null) {
            LogUtil.infof(log, "login fail");
            return ResultCodeUtil.buildCommonResponseSimple(new LoginResponse(), null, ResultCodeEnum.PSW_NOT_EQUALS);
        }

        LogUtil.infof(log, "login success");
        return ResultCodeUtil.buildCommonResponseSimple(new LoginResponse(), userWithAuthToken.getExternParam().get("authToken").toString(), ResultCodeEnum.OK);
    }

    @Operation(summary = "获取用户详情")
    @GetMapping(value = "/auth/userinfo")
    public UserInfoResponse checkLogin(HttpServletRequest httpServletRequest) {
        String authToken = httpServletRequest.getHeader("authToken");
        if (StringUtils.isNotEmpty(authToken)) {
            User user = userCenterService.getUserByAuthToken(authToken);
            if (user != null) {
                return ResultCodeUtil.buildCommonResponseSimple(new UserInfoResponse(), user, ResultCodeEnum.OK);
            }
        }

        // authToken失效，判断ip
        String ip = IpUtil.getIpAddr(httpServletRequest);
        if (StringUtils.isEmpty(ip)) {
            LogUtil.errorf(log, "ip invalid, {}", ip);
            return ResultCodeUtil.buildCommonResponseSimple(new UserInfoResponse(), null, ResultCodeEnum.NEED_LOGIN);
        }

        User user = userCenterService.getUserByIp(ip);
        if (user == null) {
            LogUtil.errorf(log, "ip not reg, {}", ip);
            return ResultCodeUtil.buildCommonResponseSimple(new UserInfoResponse(), null, ResultCodeEnum.NEED_LOGIN);
        }

        return ResultCodeUtil.buildCommonResponseSimple(new UserInfoResponse(), user, ResultCodeEnum.NEED_LOGIN);
    }
}
