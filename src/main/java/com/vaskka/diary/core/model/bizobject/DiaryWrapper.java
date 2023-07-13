package com.vaskka.diary.core.model.bizobject;

import lombok.Data;

import java.util.List;

@Data
public class DiaryWrapper {

    private Author author;

    private String diaryTitle;

    List<Diary> diaryList;

}
