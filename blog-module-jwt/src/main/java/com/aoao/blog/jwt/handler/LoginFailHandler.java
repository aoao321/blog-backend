package com.aoao.blog.jwt.handler;

import com.aoao.blog.common.enums.ResponseCodeEnum;
import com.aoao.blog.jwt.exception.UsernameOrPasswordNullException;
import com.aoao.blog.common.utils.Result;
import com.aoao.blog.jwt.utils.HttpResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author aoao
 * @create 2025-07-13-18:17
 */
@Component
@Slf4j
public class LoginFailHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        if (exception instanceof UsernameOrPasswordNullException) {
            log.warn("用户名或密码为空: {}", exception.getMessage());
            HttpResultUtil.fail(response, Result.fail(exception.getMessage()));
        } else if (exception instanceof BadCredentialsException) {
            log.warn("用户名或密码错误");
            HttpResultUtil.fail(response, Result.fail(ResponseCodeEnum.USERNAME_OR_PWD_ERROR));
        } else {
            log.warn("登录失败: {}", exception.getMessage());
            HttpResultUtil.fail(response, Result.fail(ResponseCodeEnum.LOGIN_FAIL));
        }
    }
}
