package com.aoao.blog.jwt.cofig;

import com.aoao.blog.jwt.filter.JwtAuthenticationFilter;
import com.aoao.blog.jwt.filter.TokenAuthenticationFilter;
import com.aoao.blog.jwt.handler.LoginFailHandler;
import com.aoao.blog.jwt.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author aoao
 * @create 2025-07-13-19:01
 */
@Configuration
public class JwtAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailHandler loginFailHandler;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        // 自定义的用于 JWT 身份验证的过滤器
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter();
        filter.setAuthenticationManager(httpSecurity.getSharedObject(AuthenticationManager.class));

        // 设置登录认证对应的处理类（成功处理、失败处理）
        filter.setAuthenticationSuccessHandler(loginSuccessHandler);
        filter.setAuthenticationFailureHandler(loginFailHandler);

        // 直接使用 DaoAuthenticationProvider, 它是 Spring Security 提供的默认的身份验证提供者之一
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // 设置 userDetailService，用于获取用户的详细信息
        provider.setUserDetailsService(userDetailsService);

        // 设置加密算法
        provider.setPasswordEncoder(passwordEncoder);
        httpSecurity.authenticationProvider(provider);

        // 将这个过滤器添加到 UsernamePasswordAuthenticationFilter 之前执行
        httpSecurity.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        // 添加JWT过滤器到UsernamePasswordAuthenticationFilter之前
        httpSecurity.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
