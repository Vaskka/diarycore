package com.vaskka.diary.core.model.bizobject;

import lombok.Data;

@Data
public class Author {

    private String authorId;

    private String authorName;

    /**
     * 日记的起止时间
     */
    private String diaryDateLineForShow;

    private String authorAvatarUrl;

    private String externParam;

}
