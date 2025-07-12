package com.aoao.blog.module.common.aspect;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * aop用于打印日志
 *
 * @author aoao
 * @create 2025-07-08-17:54
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ApiOperationLog {
    String description() default "";
}
