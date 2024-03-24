package com.vaskka.diary.core.config;
import com.vaskka.diary.core.filter.UserAuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class WebConfig {

    @Resource
    private UserAuthFilter userAuthFilter;

    @Bean
    public FilterRegistrationBean<UserAuthFilter> userAuthFilterRegisterBean() {
        FilterRegistrationBean<UserAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(userAuthFilter);
        // 设置过滤器的顺序，数值越小，优先级越高
        registrationBean.setOrder(1);
        // 指定过滤所有请求
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}