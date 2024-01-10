package com.vaskka.diary.core.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Configuration
public class TimeZoneConfig {

    @PostConstruct
    public void init() {
        // 设置时区为UTC
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }
}
