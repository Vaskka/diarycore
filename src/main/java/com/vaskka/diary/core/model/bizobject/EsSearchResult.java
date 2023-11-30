package com.vaskka.diary.core.model.bizobject;

import lombok.Data;

import java.util.List;

@Data
public class EsSearchResult {

    @Data
    public static class DateWithCountResult {
        private String date;

        private Long count;
    }

    private List<DiaryContent> data;

    private Long totalRecordCount;

    private Integer totalPage;

    private Integer sizeOfPage;

    private List<DateWithCountResult> aggDateWithCount;
}
