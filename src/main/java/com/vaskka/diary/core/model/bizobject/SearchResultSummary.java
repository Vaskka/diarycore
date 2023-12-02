package com.vaskka.diary.core.model.bizobject;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SearchResultSummary {

    /**
     * authorId -> authorName
     */
    private Map<String, String> authorMap;

    /**
     * authorId -> authorCount
     */
    private Map<String, Long> authorCountMap;

    private List<EsSearchResult.DateWithCountResult> aggDateWithCount;

    private List<EsSearchResult.DateWithCountResult> aggDateWithCountMonth;
}
