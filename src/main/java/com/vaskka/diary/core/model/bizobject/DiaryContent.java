package com.vaskka.diary.core.model.bizobject;

import lombok.Data;

import java.util.List;

/**
 * 日记的内容
 * 后续可扩展注释之类的功能
 */
@Data
public class DiaryContent {

    private String diaryId;

    private String authorId;

    private String content;

    private String title;

    private String subTitle;

    private Long timestamp;

    private String comment;

    private List<String> userIdList;

}
