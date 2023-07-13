package com.vaskka.diary.core.facade;

import com.vaskka.diary.core.model.bizobject.Diary;
import com.vaskka.diary.core.model.bizobject.DiaryWrapper;
import com.vaskka.diary.core.model.bizobject.SearchCondition;

import java.util.List;

public interface DiaryFacade {

    /**
     * 根据作者查找某篇日记
     * @param authorId authorId 作者id
     * @return DiaryWrapper
     */
    DiaryWrapper findByAuthor(String authorId);

    /**
     * find by subId
     * @param diarySubId diarySubId
     * @return Diary 某篇具体的日记
     */
    Diary findSubDiaryById(String diarySubId);

    /**
     * simple search
     * @param searchCondition 查找condition
     * @return list
     */
    List<Diary> simpleSearch(SearchCondition searchCondition);


}
