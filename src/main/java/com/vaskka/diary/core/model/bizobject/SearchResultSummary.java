package com.vaskka.diary.core.model.bizobject;

import lombok.Data;

import java.util.HashMap;
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
    private Map<String, Integer> authorCountMap;

    private SearchResultSummary() { }

    public static SearchResultSummary buildSummary(List<Diary> resultDiary) {
        SearchResultSummary summary = new SearchResultSummary();
        Map<String, String> authorMap = new HashMap<>();
        Map<String, Integer> authorCountMap = new HashMap<>();
        for (var diary : resultDiary) {
            if (!authorMap.containsKey(diary.getAuthorId())) {
                authorMap.put(diary.getAuthorId(), diary.getAuthorName());
                authorCountMap.put(diary.getAuthorId(), 1);
            } else {
                authorCountMap.put(diary.getAuthorId(), authorCountMap.get(diary.getAuthorId()) + 1);
            }
        }

        summary.setAuthorMap(authorMap);
        summary.setAuthorCountMap(authorCountMap);
        return summary;
    }

}
