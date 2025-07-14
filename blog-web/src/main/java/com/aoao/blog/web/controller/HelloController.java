package com.aoao.blog.web.controller;

import com.aoao.blog.common.aspect.ApiOperationLog;
import com.aoao.blog.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * @author aoao
 * @create 2025-07-08-19:26
 */
@RestController
@Slf4j
public class HelloController {

    @ApiOperationLog(description = "你好啊")
    @GetMapping("/admin/hello")
    public Result hello(String name) {
        LocalDateTime now = LocalDateTime.now();
        return Result.success(now);
    }

    @PostMapping("/admin/update")
    @ApiOperationLog(description = "测试更新接口")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result testUpdate() {
        log.info("更新成功...");
        return Result.success();
    }

}

