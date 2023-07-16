package com.vaskka.diary.core.controller;

import com.vaskka.diary.core.constant.ResultCodeEnum;
import com.vaskka.diary.core.facade.AuthorFacade;
import com.vaskka.diary.core.model.viewobject.AuthorListResponse;
import com.vaskka.diary.core.model.viewobject.NeedAuthRequest;
import com.vaskka.diary.core.utils.ResultCodeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Tag(name = "作者接口")
@RequestMapping(value = "/v1/api/author")
@RestController
public class AuthorController {

    @Resource
    private AuthorFacade authorServiceImpl;

    @Operation(summary = "获取全部作者")
    @PostMapping(value = "/all")
    public AuthorListResponse allAuthor(@RequestBody NeedAuthRequest request) {
        var data = authorServiceImpl.findAll();
        return ResultCodeUtil.buildCommonResponse(AuthorListResponse::new, data, ResultCodeEnum.OK);
    }

}
