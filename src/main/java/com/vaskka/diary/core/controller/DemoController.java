package com.vaskka.diary.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.vaskka.diary.core.constant.ResultCodeEnum;
import com.vaskka.diary.core.facade.AuthFacade;
import com.vaskka.diary.core.model.viewobject.CommonResponse;
import com.vaskka.diary.core.model.viewobject.NeedAuthRequest;
import com.vaskka.diary.core.facade.DemoService;
import com.vaskka.diary.core.utils.ResultCodeUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class DemoController extends NeedAuthController {

    @Resource
    private DemoService demoService;

    @Resource
    private AuthFacade authServiceImpl;

    @GetMapping(value = "/v1/api/demo")
    public String demo() {
        return demoService.demo();
    }

    @GetMapping(value = "/v1/api/demo/auth/login")
    public String login() {
        var res = authServiceImpl.localLogin("123");
        return res.getAuthToken();
    }

    @PostMapping(value = "/v1/api/demo/auth/check/login")
    public CommonResponse checkLogin(@RequestBody NeedAuthRequest request) {
        var auth = auth(request);
        var res = ResultCodeUtil.buildCommonResponse(CommonResponse::new, ResultCodeEnum.OK);
        res.setResultMsg(res.getResultMsg() + "-" + JSONObject.toJSONString(auth));
        return res;
    }

}
