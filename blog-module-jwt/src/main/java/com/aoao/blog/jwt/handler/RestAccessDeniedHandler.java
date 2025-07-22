package com.aoao.blog.jwt.handler;

import com.aoao.blog.common.enums.ResponseCodeEnum;
import com.aoao.blog.common.utils.Result;
import com.aoao.blog.jwt.utils.HttpResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author aoao
 * @create 2025-07-14-10:08
 */
@Slf4j
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("登录成功访问收保护的资源，但是权限不够:{} ", accessDeniedException.getMessage());
        HttpResultUtil.fail(response, Result.fail(ResponseCodeEnum.FORBIDDEN));
    }
}
