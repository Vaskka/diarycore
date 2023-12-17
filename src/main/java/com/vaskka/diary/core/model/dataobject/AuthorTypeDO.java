package com.vaskka.diary.core.model.dataobject;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthorTypeDO {

    private Long id;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private Long authorId;

    private String authorTypeName;

    private String externParam;
}
