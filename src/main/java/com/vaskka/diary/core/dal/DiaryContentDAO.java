package com.vaskka.diary.core.dal;

import com.vaskka.diary.core.model.bizobject.DiaryContent;
import com.vaskka.diary.core.model.bizobject.EsSearchResult;

import java.util.List;
import java.util.Optional;

public interface DiaryContentDAO {

    /**
     * create one
     * @param diaryContent diary content
     */
    void insertDiaryContent(DiaryContent diaryContent);

    /**
     * find by id
     * @param diaryId diary id
     * @return optional
     */
    Optional<DiaryContent> findById(String diaryId);

    /**
     * sample search
     * only related result of list
     * @param searchText search key text
     * @return list
     */
    List<DiaryContent> simpleSearch(String searchText);

    /**
     * search pageable
     *
     * @param searchText text
     * @param size       size
     * @param page       page
     * @return list
     */
    EsSearchResult searchPageable(String searchText, Integer size, Integer page);
}
