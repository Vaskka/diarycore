package com.vaskka.diary.core.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class CommonUtil {

    /**
     * 日期格式字符串
     */
    public static final String DATE_FORMAT_NORMAL = "yyyy-MM-dd";

    /**
     * 精确到秒时间格式化字符串
     */
    public static final String DATETIME_FORMAT_NORMAL = "yyyy-MM-dd HH:mm:ss";

    /**
     * 精确到毫秒时间格式化字符串
     */
    public static final String DATETIME_FORMAT_DETAIL = "yyyy-MM-dd HH:mm:ss.SSS";

    public static String getRandom32LengthStr() {
        return DigestUtils.md5Hex(UUID.randomUUID().toString());
    }

    public static String getDateStr(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone).format(DateTimeFormatter.ofPattern(DATE_FORMAT_NORMAL));
    }

    public static String getDateTimeStr(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone).format(DateTimeFormatter.ofPattern(DATETIME_FORMAT_NORMAL));
    }

    public static String getDateTimeDetailStr(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone).format(DateTimeFormatter.ofPattern(DATETIME_FORMAT_DETAIL));
    }
}
