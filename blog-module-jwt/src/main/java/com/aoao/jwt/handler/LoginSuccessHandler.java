package com.aoao.jwt.handler;

import com.aoao.blog.common.utils.Result;
import com.aoao.jwt.utils.HttpResultUtil;
import com.aoao.jwt.utils.JwtTokenHelper;
import com.aoao.jwt.vo.LoginTokenVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author aoao
 * @create 2025-07-13-18:04
 */
@Component
@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    // 认证成功
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 把用户名转换成token然后返回
        UserDetails user = (UserDetails)authentication.getPrincipal();

        // 通过用户名生成 Token
        String username = user.getUsername();
        String token = jwtTokenHelper.generateToken(username);

        // 封装返回对象
        LoginTokenVo loginTokenVo = new LoginTokenVo(token);
        HttpResultUtil.ok(response,Result.success(loginTokenVo));

    }
}
