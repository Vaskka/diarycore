package com.vaskka.diary.core.model.bizobject;

import lombok.Data;

import java.util.List;

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

    private List<DiarySummary> summaryList;

}
