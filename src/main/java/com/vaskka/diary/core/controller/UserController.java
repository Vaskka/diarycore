package com.vaskka.diary.core.controller;

import com.vaskka.diary.core.constant.ResultCodeEnum;
import com.vaskka.diary.core.facade.UserCenterFacade;
import com.vaskka.diary.core.model.viewobject.RegisterRequest;
import com.vaskka.diary.core.model.viewobject.RegisterResponse;
import com.vaskka.diary.core.utils.ResultCodeUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping(value = "/v1/api/user")
@RestController
public class UserController {

    @Resource
    private UserCenterFacade userCenterService;

    @PostMapping(value = "/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        var user = userCenterService.register(request.getUserName(), request.getAvatarUrl(), request.getPsw());
        var resp = ResultCodeUtil.buildCommonResponse(RegisterResponse::new, ResultCodeEnum.OK);
        resp.setUser(user);
        return resp;
    }

}
