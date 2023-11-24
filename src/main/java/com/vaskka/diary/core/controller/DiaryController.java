package com.vaskka.diary.core.controller;

import com.vaskka.diary.core.constant.ResultCodeEnum;
import com.vaskka.diary.core.facade.DiaryFacade;
import com.vaskka.diary.core.model.bizobject.Diary;
import com.vaskka.diary.core.model.bizobject.SearchCondition;
import com.vaskka.diary.core.model.bizobject.SearchSummaryResult;
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
import java.util.stream.Collectors;

@Slf4j
@Tag(name = "日记接口")
@RequestMapping(value = "/v1/api/diary")
@RestController
public class DiaryController extends NeedAuthController {

    @Resource
    private DiaryFacade diaryServiceImpl;

    @Operation(summary = "根据作者获取作者全部的日记")
    @PostMapping(value = "/find/author/{authorId}")
    public DiaryWrapperResponse findByAuth(@PathVariable(value = "authorId") String authorId,
                                           @RequestBody NeedAuthRequest request) {
        var data = diaryServiceImpl.findByAuthor(authorId);
        return ResultCodeUtil.buildCommonResponse(DiaryWrapperResponse::new, data, ResultCodeEnum.OK);
    }

    @Operation(summary = "根据作者获取作者全部的日记，分页")
    @PostMapping(value = "/find/author/pageable/{authorId}/{page}/{size}")
    public DiaryWrapperResponse findByAuthPageable(@PathVariable(value = "authorId") String authorId,
                                                   @PathVariable(value = "page") Integer page,
                                                   @PathVariable(value = "size") Integer size,
                                                   @RequestBody NeedAuthRequest request) {
        var data = diaryServiceImpl.findByAuthorPageable(authorId, page, size);
        return ResultCodeUtil.buildCommonResponse(DiaryWrapperResponse::new, data, ResultCodeEnum.OK);
    }

    @Operation(summary = "根据作者获取作者全部的日记")
    @PostMapping(value = "/find/author/{authorId}/{date}")
    public DiaryWrapperResponse findByAuthAndDate(@PathVariable(value = "authorId") String authorId,
                                                  @PathVariable(value = "date") String date,
                                                  @RequestBody NeedAuthRequest request) {
        var data = diaryServiceImpl.findByAuthorAndDate(authorId, date);
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
        return ResultCodeUtil.buildCommonResponse(SingleDiaryResponse::new, data, ResultCodeEnum.OK);
    }

    @Operation(summary = "搜索")
    @PostMapping(value = "/search")
    public SearchDiaryResponse search(@RequestBody SearchDiaryRequest request) {
        LogUtil.infof(log, "[search],search key={},", request.getSearchText());
        var searchCondition = new SearchCondition();
        searchCondition.setSearchText(request.getSearchText());
        searchCondition.setAuthorIdPicker(SearchCondition.MultiPicker.build(request.getPickedAuthorId()));

        var rawData = diaryServiceImpl.simpleSearch(searchCondition);
        var data = summary(rawData);
        LogUtil.infof(log, "[search],final result:{}", data);
        return ResultCodeUtil.buildCommonResponse(SearchDiaryResponse::new, data, ResultCodeEnum.OK);
    }

    private SearchSummaryResult summary(List<Diary> allDiaryList) {
        var result = new SearchSummaryResult();

        // 聚类作者
        List<String> authorNameList = allDiaryList.stream()
                .map(Diary::getAuthorName)
                .distinct()
                .collect(Collectors.toList());

        result.setAuthorNameList(authorNameList);

        // 统计聚合搜索结果
        Map<String, SearchSummaryResult.DiarySummary> summaryMap = new HashMap<>();
        for (var diary : allDiaryList) {
            String year = CommonUtil.parseStrDate2Year(diary.getDiaryDateTimeStr());
            String month = CommonUtil.parseStrDate2Month(diary.getDiaryDateTimeStr());

            if (summaryMap.containsKey(year)) {
                var summary = summaryMap.get(year);
                var monthList = summary.getMonthList();
                var dateInstrList = summary.getDateInStrList();
                var diaryList = summary.getDiaryList();

                if (monthList.contains(month)) {
                    int monthIndex = monthList.indexOf(month);
                    dateInstrList.get(monthIndex).add(diary.getDiaryDateTimeStr());
                    diaryList.get(monthIndex).add(diary);
                } else {
                    monthList.add(month);

                    List<Diary> newDiaryList = new ArrayList<>();
                    newDiaryList.add(diary);
                    diaryList.add(newDiaryList);
                    diaryList.sort(Comparator.comparing(e -> CommonUtil.parseStrDate2MonthInt(e.get(0).getDiaryDateTimeStr())));

                    List<String> newDateInStr = new ArrayList<>();
                    newDateInStr.add(diary.getDiaryDateTimeStr());
                    dateInstrList.add(newDateInStr);
                    dateInstrList.sort(Comparator.comparing(e -> CommonUtil.parseStrDate2MonthInt(e.get(0))));
                }
            } else {
                // init new one
                SearchSummaryResult.DiarySummary diarySummary = new SearchSummaryResult.DiarySummary();
                diarySummary.setYear(year);
                diarySummary.setMonthList(new ArrayList<>());
                diarySummary.setDiaryList(new ArrayList<>());
                diarySummary.setDateInStrList(new ArrayList<>());

                diarySummary.getMonthList().add(month);

                List<Diary> firstDiaryList = new ArrayList<>();
                firstDiaryList.add(diary);
                diarySummary.getDiaryList().add(firstDiaryList);

                List<String> firstDateInStr = new ArrayList<>();
                firstDateInStr.add(diary.getDiaryDateTimeStr());
                diarySummary.getDateInStrList().add(firstDateInStr);

                summaryMap.put(year, diarySummary);
            }

        }
        result.setSummaryList(summaryMap.values()
                .stream()
                .sorted(Comparator.comparing(e -> Integer.parseInt(e.getYear())))
                .collect(Collectors.toList()));

        return result;
    }

}
