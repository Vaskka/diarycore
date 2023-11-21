package com.vaskka.diary.core;

import com.vaskka.diary.core.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

@Slf4j
public class CommonTests {

    @Test
    public void demo() {
        log.info(String.valueOf(CommonUtil.parseStrDate2Timestamp("1892-01-01")));
    }

}
