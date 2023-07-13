package com.vaskka.diary.core.model.bizobject;

import lombok.Data;

/**
 * 日记的内容
 * 后续可扩展注释之类的功能
 */
@Data
public class DiaryContent {

    private String diaryId;

    private String content;

}