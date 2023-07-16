package com.vaskka.diary.core.controller;

import com.vaskka.diary.core.constant.ResultCodeEnum;
import com.vaskka.diary.core.facade.DiaryFacade;
import com.vaskka.diary.core.model.bizobject.SearchCondition;
import com.vaskka.diary.core.model.viewobject.*;
import com.vaskka.diary.core.utils.ResultCodeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Tag(name = "日记接口")
@RequestMapping(value = "/v1/api/diary")
@RestController
public class DiaryController extends NeedAuthController {

    @Resource
    private DiaryFacade diaryServiceImpl;

    @Operation(summary = "根据作者获取作者全部的日记")
    @PostMapping(value = "/find/author/{authorId}")
    public DiaryWrapperResponse findByAuth(@PathVariable(value = "authorId") String authorId, @RequestBody NeedAuthRequest request) {
        var data = diaryServiceImpl.findByAuthor(authorId);
        return ResultCodeUtil.buildCommonResponse(DiaryWrapperResponse::new, data, ResultCodeEnum.OK);
    }

    @Operation(summary = "获取某个具体日记的详细内容")
    @PostMapping(value = "/detail/{diarySubId}")
    public SingleDiaryResponse detailBySubId(@PathVariable(value = "diarySubId") String diarySubId, @RequestBody NeedAuthRequest request) {
        var data = diaryServiceImpl.findSubDiaryById(diarySubId);
        return ResultCodeUtil.buildCommonResponse(SingleDiaryResponse::new, data, ResultCodeEnum.OK);
    }

    @Operation(summary = "搜索")
    @PostMapping(value = "/search")
    public SearchDiaryResponse search(@RequestBody SearchDiaryRequest request) {
        var searchCondition = new SearchCondition();
        searchCondition.setSearchText(request.getSearchText());
        searchCondition.setAuthorIdPicker(SearchCondition.MultiPicker.build(request.getPickedAuthorId()));

        var data = diaryServiceImpl.simpleSearch(searchCondition);
        return ResultCodeUtil.buildCommonResponse(SearchDiaryResponse::new, data, ResultCodeEnum.OK);
    }


}
