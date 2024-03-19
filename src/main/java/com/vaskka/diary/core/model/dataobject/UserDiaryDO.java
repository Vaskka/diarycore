package com.vaskka.diary.core.model.dataobject;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDiaryDO {

    private Long id;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private Long userId;

    private Long diaryId;

}
