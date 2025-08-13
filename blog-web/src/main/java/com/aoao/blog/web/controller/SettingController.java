package com.aoao.blog.web.controller;

import com.aoao.blog.common.domain.dos.BlogSettingsDO;
import com.aoao.blog.common.domain.mapper.BlogSettingMapper;
import com.aoao.blog.common.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author aoao
 * @create 2025-07-25-12:41
 */
@RestController
@RequestMapping("/blog/settings")
@Api("设置相关")
public class SettingController {

    @Autowired
    private BlogSettingMapper blogSettingMapper;

    @PostMapping("/detail")
    @ApiOperation("详细")
    public Result<BlogSettingsDO> detail() {
        BlogSettingsDO settingsDO = blogSettingMapper.selectById(1l);
        return Result.success(settingsDO);
    }
}
