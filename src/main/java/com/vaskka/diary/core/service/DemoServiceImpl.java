package com.vaskka.diary.core.service;

import com.vaskka.diary.core.facade.DemoService;
import com.vaskka.diary.core.utils.CommonUtil;
import com.vaskka.diary.core.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service("demoService")
public class DemoServiceImpl implements DemoService {
    @Override
    public String demo() {
        var res = "hello-" + LocalDateTime.now() + "-" + CommonUtil.getRandom32LengthStr();
        LogUtil.infof(log, "msg:{}", res);
        return res;
    }
}
