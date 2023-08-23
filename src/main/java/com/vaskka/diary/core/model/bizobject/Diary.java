package com.vaskka.diary.core.model.bizobject;

import lombok.Data;


@Data
public class Diary {

    /**
     * 某篇具体的日记的id,暂时也用作content内容的id
     */
    private String diaryId;

    private String authorId;

    private String authorName;

    private String subTitle;

    /**
     * 日记记录的时间戳
     */
    private Long diaryDateTimestamp;

    /**
     * 日记记录的日期 字符串
     */
    private String diaryDateTimeStr;

    private String startPage;

    private String endPage;

    private String originPic;

    private String comment;

    /**
     * 日记的内容
     */
    private DiaryContent diaryContent;

    private String externParam;
}
