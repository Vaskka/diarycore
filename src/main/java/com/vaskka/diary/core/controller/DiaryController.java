package com.vaskka.diary.core.controller;

import com.vaskka.diary.core.constant.ResultCodeEnum;
import com.vaskka.diary.core.facade.DiaryAuthManager;
import com.vaskka.diary.core.facade.DiaryFacade;
import com.vaskka.diary.core.model.bizobject.SearchCondition;
import com.vaskka.diary.core.model.viewobject.*;
import com.vaskka.diary.core.utils.CommonUtil;
import com.vaskka.diary.core.utils.LogUtil;
import com.vaskka.diary.core.utils.ResultCodeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Tag(name = "日记接口")
@RequestMapping(value = "/v1/api/diary")
@RestController
public class DiaryController extends NeedAuthController {

    @Resource
    private DiaryFacade diaryServiceImpl;

    @Resource
    private DiaryAuthManager defaultDiaryAuthManager;

    @Operation(summary = "根据作者获取作者全部的日记")
    @PostMapping(value = "/find/author/{authorId}")
    public DiaryWrapperResponse findByAuth(@PathVariable(value = "authorId") String authorId,
                                           @RequestBody NeedAuthRequest request) {
        var data = diaryServiceImpl.findByAuthor(authorId);
        data = defaultDiaryAuthManager.filterPermissionDiaryWrapper(request.getUser(), data);
        return ResultCodeUtil.buildCommonResponse(DiaryWrapperResponse::new, data, ResultCodeEnum.OK);
    }

    @Operation(summary = "根据作者获取作者全部的日记，分页")
    @PostMapping(value = "/find/author/pageable/{authorId}/{page}/{size}")
    public DiaryWrapperResponse findByAuthPageable(@PathVariable(value = "authorId") String authorId,
                                                   @PathVariable(value = "page") Integer page,
                                                   @PathVariable(value = "size") Integer size,
                                                   @RequestBody NeedAuthRequest request) {
        var data = diaryServiceImpl.findByAuthorPageable(authorId, page, size);
        data = defaultDiaryAuthManager.filterPermissionDiaryWrapper(request.getUser(), data);
        return ResultCodeUtil.buildCommonResponse(DiaryWrapperResponse::new, data, ResultCodeEnum.OK);
    }

    @Operation(summary = "根据作者获取作者全部的日记")
    @PostMapping(value = "/find/author/{authorId}/{date}")
    public DiaryWrapperResponse findByAuthAndDate(@PathVariable(value = "authorId") String authorId,
                                                  @PathVariable(value = "date") String date,
                                                  @RequestBody NeedAuthRequest request) {
        var data = diaryServiceImpl.findByAuthorAndDate(authorId, date);
        data = defaultDiaryAuthManager.filterPermissionDiaryWrapper(request.getUser(), data);
        return ResultCodeUtil.buildCommonResponse(DiaryWrapperResponse::new, data, ResultCodeEnum.OK);
    }

    @Operation(summary = "根据作者获取作者全部的日记的起止日期")
    @PostMapping(value = "/find/diary/between/{authorId}")
    public CommonResponse<List<String>> getDiaryBetween(@PathVariable(value = "authorId") String authorId,
                                                  @RequestBody NeedAuthRequest request) {
        var innerData = diaryServiceImpl.findDiaryBetween(authorId);
        return ResultCodeUtil.buildCommonResponse(CommonResponse::new, innerData, ResultCodeEnum.OK);
    }

    @Operation(summary = "根据作者获取作者全部的日记的日期")
    @PostMapping(value = "/find/diary/datetime/{authorId}")
    public CommonResponse<List<String>> getDiaryDateTime(@PathVariable(value = "authorId") String authorId,
                                                        @RequestBody NeedAuthRequest request) {
        var innerData = diaryServiceImpl.findDateTimeByAuthor(authorId);
        return ResultCodeUtil.buildCommonResponse(CommonResponse::new, innerData, ResultCodeEnum.OK);
    }

    @Operation(summary = "获取某个具体日记的详细内容")
    @PostMapping(value = "/detail/{diarySubId}")
    public SingleDiaryResponse detailBySubId(@PathVariable(value = "diarySubId") String diarySubId, @RequestBody NeedAuthRequest request) {
        var data = diaryServiceImpl.findSubDiaryById(diarySubId);

        if (request.getUser() == null) {
            return ResultCodeUtil.buildCommonResponse(SingleDiaryResponse::new, null, ResultCodeEnum.OK);
        } else {
            if (defaultDiaryAuthManager.checkDiaryPermission(Long.parseLong(request.getUser().getUid()), Long.parseLong(data.getDiaryId()))) {
                return ResultCodeUtil.buildCommonResponse(SingleDiaryResponse::new, data, ResultCodeEnum.OK);
            } else {
                return ResultCodeUtil.buildCommonResponse(SingleDiaryResponse::new, null, ResultCodeEnum.OK);
            }
        }
    }

    @Operation(summary = "高级搜索")
    @PostMapping(value = "/pageable/search/multi/{size}/{page}")
    public SearchDiaryPageableResponse searchV3(@PathVariable("size") Integer size, @PathVariable("page") Integer page,
                                              @RequestBody SearchDiaryRequest request) {
        LogUtil.infof(log, "[searchV3],search key={},", request.getSearchText());
        var searchCondition = new SearchCondition();
        searchCondition.setUser(request.getUser());
        searchCondition.setPage(page);
        searchCondition.setSize(size);
        searchCondition.setAuthorIdNotIn(request.getAuthorIdNotIn());
        searchCondition.setMultiSearchText(request.getMultiSearchText());
        if (request.getDateRange() != null) {
            if (request.getDateRange().getGe().length() == 4) {
                request.getDateRange().setGe(request.getDateRange().getGe() + "-01-01");
            } else if (request.getDateRange().getGe().length() == 7) {
                request.getDateRange().setGe(request.getDateRange().getGe() + "-01");
            }
            long gte = CommonUtil.parseStrDate2Timestamp(request.getDateRange().getGe());

            if (request.getDateRange().getLe().length() == 4) {
                request.getDateRange().setLe(request.getDateRange().getLe() + "-01-01");
            } else if (request.getDateRange().getLe().length() == 7) {
                request.getDateRange().setLe(request.getDateRange().getLe() + "-01");
            }
            long lte = CommonUtil.parseStrDate2Timestamp(request.getDateRange().getLe());
            searchCondition.setTimestampRange(SearchCondition.RangePicker.getInstance(gte, lte));
        }

        var rawData = diaryServiceImpl.searchV3(searchCondition);
        return ResultCodeUtil.buildCommonResponse(SearchDiaryPageableResponse::new, rawData, ResultCodeEnum.OK);
    }

}
