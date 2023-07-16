package com.vaskka.diary.core.controller;

import com.vaskka.diary.core.facade.DemoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class DemoController extends NeedAuthController {

    @Resource
    private DemoService demoService;

    @GetMapping(value = "/v1/api/demo")
    public String demo() {
        return demoService.demo();
    }

}
