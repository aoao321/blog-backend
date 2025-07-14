package com.aoao.blog.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author aoao
 * @create 2025-07-13-16:21
 */
@Configuration
@MapperScan("com.aoao.blog.common.domain.mapper")
public class MybatisPlusConfig {
}
