package com.vaskka.diary.core.filter;

import com.alibaba.fastjson.JSONObject;
import com.vaskka.diary.core.facade.UserCenterFacade;
import com.vaskka.diary.core.model.bizobject.User;
import com.vaskka.diary.core.model.web.ModifiedHttpServletRequest;
import com.vaskka.diary.core.utils.IpUtil;
import com.vaskka.diary.core.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class UserAuthFilter implements Filter {

    @Resource
    private UserCenterFacade userCenterService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        if (!HttpMethod.POST.matches(httpServletRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        // 获取原始请求体内容
        byte[] originalRequestBodyBytes = httpServletRequest.getInputStream().readAllBytes();
        String originalRequestBody = new String(originalRequestBodyBytes, StandardCharsets.UTF_8);

        // 修改请求体内容, 注入user
        String modifiedRequestBody = modifyRequestBody(httpServletRequest, originalRequestBody);

        // 创建自定义请求包装器，包含修改后的请求体
        ModifiedHttpServletRequest modifiedRequest = new ModifiedHttpServletRequest(httpServletRequest, modifiedRequestBody);

        // 继续过滤器链
        chain.doFilter(modifiedRequest, response);
    }

    private String modifyRequestBody(HttpServletRequest httpServletRequest, String originalRequestBody) {
        String authToken = httpServletRequest.getHeader("authToken");
        if (StringUtils.isNotEmpty(authToken)) {
            try {
                User user = userCenterService.getUserByAuthToken(authToken);
                if (user != null) {
                    return buildFinalBody(user, originalRequestBody);
                } else {
                    LogUtil.errorf(log, "authToken:{} invalid", authToken);
                }
            } catch (Exception e) {
                LogUtil.errorf(log, "getUserByAuthToken err");
                return originalRequestBody;
            }
        }

        // authToken失效，判断ip
        String ip = IpUtil.getIpAddr(httpServletRequest);
        if (StringUtils.isEmpty(ip)) {
            LogUtil.errorf(log, "ip invalid, {}", ip);
            return buildFinalBody(null, originalRequestBody);
        }

        try {
            User user = userCenterService.getUserByIp(ip);
            return buildFinalBody(user, originalRequestBody);
        } catch (Exception e) {
            LogUtil.errorf(log, "getUserByIp err", e);
            return originalRequestBody;
        }
    }

    private String buildFinalBody(User user, String originalRequestBody) {
        if (user == null) {
            // user 不存在 直接返回
            return originalRequestBody;
        }

        if (originalRequestBody == null) {
            return null;
        }

        LogUtil.infof(log, "auth success,user={}", user);
        JSONObject jsonBody = JSONObject.parseObject(originalRequestBody);
        jsonBody.put("user", user);
        return jsonBody.toJSONString();
    }
}