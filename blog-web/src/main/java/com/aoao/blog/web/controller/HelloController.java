package com.aoao.blog.web.controller;

import com.aoao.blog.module.common.aspect.ApiOperationLog;
import com.aoao.blog.module.common.enums.ResponseCodeEnum;
import com.aoao.blog.module.common.exception.BizException;
import com.aoao.blog.module.common.utils.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author aoao
 * @create 2025-07-08-19:26
 */
@RestController
public class HelloController {

    @ApiOperationLog(description = "你好啊")
    @RequestMapping("/hello")
    public Result hello(String name) {
        LocalDateTime now = LocalDateTime.now();
        return Result.success(now);

    }
}

