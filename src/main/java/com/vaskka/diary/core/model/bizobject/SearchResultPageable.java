package com.vaskka.diary.core.model.bizobject;

import lombok.Data;

import java.util.List;

@Data
public class SearchResultPageable {

    private List<Diary> resultList;

    private SearchResultSummary searchResultSummary;

    private Long totalRecordCount;

    private Integer totalPage;

    private Integer sizeOfPage;

}
