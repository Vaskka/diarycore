package com.vaskka.diary.core.model.bizobject;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SearchSummaryResult {

    @Data
    public static class DiarySummary {
        private String year;

        private List<String> monthList;

        private List<List<String>> dateInStrList;

        private List<List<Diary>> diaryList;

    }

    private List<String> authorNameList;

    private Map<String, Integer> authorCountMap;

    private List<DiarySummary> summaryList;

}
