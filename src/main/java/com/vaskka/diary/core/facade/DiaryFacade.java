package com.vaskka.diary.core.facade;

import com.vaskka.diary.core.model.bizobject.Diary;
import com.vaskka.diary.core.model.bizobject.DiaryWrapper;
import com.vaskka.diary.core.model.bizobject.SearchCondition;

import java.util.List;

public interface DiaryFacade {

    /**
     * 根据作者查找日记
     * @param authorId authorId 作者id
     * @return DiaryWrapper
     */
    DiaryWrapper findByAuthor(String authorId);

    /**
     * 根据作者查找日记
     * @param authorId authorId 作者id
     * @param page page
     * @param size size
     * @return DiaryWrapper
     */
    DiaryWrapper findByAuthorPageable(String authorId, Integer page, Integer size);

    /**
     * 根据作者和日期查找日记
     * @param authorId 作者id
     * @param dateInStr 字符串类型的日期
     * @return DiaryWrapper
     */
    DiaryWrapper findByAuthorAndDate(String authorId, String dateInStr);

    /**
     * 查找作者的日记between
     * @param authorId authorId
     * @return list
     */
    List<String> findDiaryBetween(String authorId);

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
