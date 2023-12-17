package com.vaskka.diary.core.controller;

import com.vaskka.diary.core.constant.ResultCodeEnum;
import com.vaskka.diary.core.facade.AuthorFacade;
import com.vaskka.diary.core.model.viewobject.AuthorListResponse;
import com.vaskka.diary.core.model.viewobject.AuthorTypeListResponse;
import com.vaskka.diary.core.model.viewobject.NeedAuthRequest;
import com.vaskka.diary.core.utils.ResultCodeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Tag(name = "作者接口")
@RequestMapping(value = "/v1/api/author")
@RestController
public class AuthorController {

    @Resource
    private AuthorFacade authorServiceImpl;

    @Deprecated
    @Operation(summary = "获取全部作者")
    @PostMapping(value = "/all")
    public AuthorListResponse allAuthor(@RequestBody NeedAuthRequest request) {
        var data = authorServiceImpl.findAll();
        return ResultCodeUtil.buildCommonResponse(AuthorListResponse::new, data, ResultCodeEnum.OK);
    }

    @Operation(summary = "全部作者的种类")
    @PostMapping(value = "/all/type")
    public AuthorTypeListResponse allAuthorType(@RequestBody NeedAuthRequest request) {
        var data = authorServiceImpl.getAllAuthorType();
        return ResultCodeUtil.buildCommonResponse(AuthorTypeListResponse::new, data, ResultCodeEnum.OK);
    }

    @Operation(summary = "根据种类查找作者列表")
    @PostMapping(value = "/type/{authorTypeName}")
    public AuthorListResponse getAuthorByType(@RequestBody NeedAuthRequest request,
                                              @PathVariable(value = "authorTypeName") String authorTypeName) {
        var data = authorServiceImpl.getAuthorByType(authorTypeName);
        return ResultCodeUtil.buildCommonResponse(AuthorListResponse::new, data, ResultCodeEnum.OK);
    }

}
