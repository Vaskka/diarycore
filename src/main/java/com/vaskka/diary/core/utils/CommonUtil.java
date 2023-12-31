package com.vaskka.diary.core.utils;

import com.google.common.collect.Lists;
import org.apache.commons.codec.digest.DigestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    public static long parseStrDate2Timestamp(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NORMAL);
        try {
            return sdf.parse(dateStr).getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getDateStrListFromDateStrMonth(String dateStr) {
        if (dateStr.split("-").length != 2) {
            return Lists.newArrayList(dateStr);
        }

        List<String> res = new ArrayList<>();
        dateStr = dateStr + "-01";
        LocalDate monthFirstDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(DATE_FORMAT_NORMAL));
        LocalDate nextMonthFirstDate = monthFirstDate.plusMonths(1);
        for (LocalDate curr = monthFirstDate; curr.isBefore(nextMonthFirstDate); curr = curr.plusDays(1)) {
            res.add(curr.format(DateTimeFormatter.ofPattern(DATE_FORMAT_NORMAL)));
        }

        return res;
    }


    public static List<Long> getTimestampListFromDateStrMonth(String dateStr) {
        if (dateStr.split("-").length != 2) {
            return Lists.newArrayList(parseStrDate2Timestamp(dateStr));
        }

        List<Long> res = new ArrayList<>();
        dateStr = dateStr + "-01";
        LocalDate monthFirstDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(DATE_FORMAT_NORMAL));
        LocalDate nextMonthFirstDate = monthFirstDate.plusMonths(1);
        for (LocalDate curr = monthFirstDate; curr.isBefore(nextMonthFirstDate); curr = curr.plusDays(1)) {
            res.add(parseLocalDate2Timestamp(curr));
        }

        return res;
    }

    public static long parseLocalDate2Timestamp(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date.getTime();
    }

    public static String parseStrDate2Year(String dateStr) {
        return String.valueOf(parseStrDate2YearInt(dateStr));
    }

    public static int parseStrDate2YearInt(String dateStr) {
        var date = parseStr2Date(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.YEAR);
    }

    public static String parseStrDate2Month(String dateStr) {
        int month = parseStrDate2MonthInt(dateStr);
        var res =  String.valueOf(month);
        if (res.length() == 1) {
            return "0" + res;
        }
        return res;
    }

    public static int parseStrDate2MonthInt(String dateStr) {
        var date = parseStr2Date(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.MONTH) + 1;
    }

    private static Date parseStr2Date(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NORMAL);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
