package com.vaskka.diary.core.model.bizobject;

import lombok.Data;

import java.util.List;

@Data
public class EsSearchResult {
    private List<DiaryContent> data;

    private Long totalRecordCount;

    private Integer totalPage;

    private Integer sizeOfPage;
}
