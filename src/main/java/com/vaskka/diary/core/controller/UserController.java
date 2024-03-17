package com.vaskka.diary.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.vaskka.diary.core.constant.ResultCodeEnum;
import com.vaskka.diary.core.facade.AuthFacade;
import com.vaskka.diary.core.facade.UserCenterFacade;
import com.vaskka.diary.core.model.viewobject.CommonResponse;
import com.vaskka.diary.core.model.viewobject.NeedAuthRequest;
import com.vaskka.diary.core.model.viewobject.RegisterRequest;
import com.vaskka.diary.core.model.viewobject.RegisterResponse;
import com.vaskka.diary.core.utils.ResultCodeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
    public String login() {
        var res = authServiceImpl.localLogin("123");
        return res.getAuthToken();
    }

    @Operation(summary = "检查登陆态")
    @PostMapping(value = "/auth/check/login")
    public CommonResponse<Object> checkLogin(@RequestBody NeedAuthRequest request) {
        var auth = auth(request);
        var res = ResultCodeUtil.buildCommonResponse(CommonResponse::new, null, ResultCodeEnum.OK);
        res.setResultMsg(res.getResultMsg() + "-" + JSONObject.toJSONString(auth));
        return res;
    }
}
