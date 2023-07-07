package com.vaskka.diary.core.model.dataobject;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DiaryDO {

    private Long id;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private Long authorId;

    private String diaryTitle;

    private String subTitle;

    private Long diaryDateTimestamp;

    private String startPage;

    private String endPage;

    private String originPic;

    private String externParam;

}
