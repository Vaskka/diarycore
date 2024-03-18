package com.vaskka.diary.core.model.dataobject;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserSessionDO {

    private Long id;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private Long userId;

    private String authToken;

    private Long expireTimestamp;
}
