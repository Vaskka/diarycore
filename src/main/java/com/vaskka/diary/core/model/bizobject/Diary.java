package com.vaskka.diary.core.model.bizobject;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Diary {
    private String diaryId;

    private Author author;

    private String diaryTitle;

    private String subTitle;

    /**
     * 日记记录的时间戳
     */
    private Long diaryDateTimestamp;

    private String startPage;

    private String endPage;

    private String originPic;

    /**
     * 日记的内容
     */
    private DiaryContent diaryContent;

    private String externParam;
}
