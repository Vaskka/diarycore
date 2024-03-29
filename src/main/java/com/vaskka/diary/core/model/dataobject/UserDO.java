package com.vaskka.diary.core.model.dataobject;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDO {

    private Long id;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private String userName;

    private String ips;

    private String userType;

    private String psw;

    private String externParam;


}
