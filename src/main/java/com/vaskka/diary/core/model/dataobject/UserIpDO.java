package com.vaskka.diary.core.model.dataobject;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserIpDO {

    private Long id;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private String ip;

    private Long refUserId;
}
