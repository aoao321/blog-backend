package com.aoao.blog.web.controller;

import com.aoao.blog.common.aspect.ApiOperationLog;
import com.aoao.blog.common.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping("/admin/hello")
    public Result hello(String name) {
        LocalDateTime now = LocalDateTime.now();
        return Result.success(now);

    }
}

