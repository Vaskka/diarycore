package com.vaskka.diary.core.controller;

import com.vaskka.diary.core.constant.ResultCodeEnum;
import com.vaskka.diary.core.facade.AdminService;
import com.vaskka.diary.core.model.viewobject.AdminUpdateUserIdRequest;
import com.vaskka.diary.core.model.viewobject.CommonResponse;
import com.vaskka.diary.core.utils.LogUtil;
import com.vaskka.diary.core.utils.ResultCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping(value = "/v1/api/admin")
public class AdminController {

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @PostMapping(value = "/update/diary/permission")
    public CommonResponse<String> updateUserIdByDiaryIds(@RequestBody AdminUpdateUserIdRequest request) {
        LogUtil.infof(log, "[admin],updateUserIdByDiaryIds,request={}", request);

        long eff = adminService.updateUserIdListByDiaryIds(request.getNewUserIdList(), request.getDiaryIds());
        LogUtil.infof(log, "[admin],updateUserIdByDiaryIds,effect={}", eff);

        return ResultCodeUtil.buildCommonResponseSimple(new CommonResponse<>(), String.valueOf(eff), ResultCodeEnum.OK);
    }

}
