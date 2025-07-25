package com.aoao.blog.admin.controller;

import com.aoao.blog.common.model.admin.vo.setting.FindBlogSettingsRspVO;
import com.aoao.blog.common.model.admin.vo.setting.UpdateBlogSettingsReqVO;
import com.aoao.blog.admin.service.AdminBlogSettingService;
import com.aoao.blog.common.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author aoao
 * @create 2025-07-22-14:49
 */
@RestController
@RequestMapping("/admin/blog/settings")
@Api("设置模块")
public class BlogSettingController {

    @Autowired
    private AdminBlogSettingService adminBlogSettingService;

    @PostMapping("/update")
    @ApiOperation("更新设置")
    public Result updateSetting(@RequestBody @Valid UpdateBlogSettingsReqVO reqVO) {
        adminBlogSettingService.updateSetting(reqVO);
        return Result.success();
    }

    @PostMapping("/detail")
    @ApiOperation("获取详情")
    public Result<FindBlogSettingsRspVO> detailSetting() {
        FindBlogSettingsRspVO vo = adminBlogSettingService.getDetail();
        return Result.success(vo);
    }
}
