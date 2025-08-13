package com.aoao.blog.admin.controller;

import com.aoao.blog.admin.service.AdminNoticeService;
import com.aoao.blog.common.model.admin.vo.notice.*;
import com.aoao.blog.common.utils.PageResult;
import com.aoao.blog.common.utils.Result;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author aoao
 * @create 2025-07-30-16:19
 */
@RestController
@RequestMapping("/admin/notice")
@Api("公告模块")
public class AdminNoticeController {

    @Autowired
    private AdminNoticeService adminNoticeService;

    @PostMapping("/add")
    @ApiOperation("新增")
    public Result add(@RequestBody @Validated AddNoticeReqVO addNoticeReqVO) {
        adminNoticeService.add(addNoticeReqVO);
        return Result.success();
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    public Result update(@RequestBody @Validated  UpdateNoticeReqVO updateNoticeReqVO) {
        adminNoticeService.update(updateNoticeReqVO);
        return Result.success();
    }

    @PostMapping("/delete")
    @ApiOperation("删除")
    public Result delete(@RequestBody @Validated DeleteNoticeReqVO deleteNoticeReqVO) {
        adminNoticeService.delete(deleteNoticeReqVO);
        return Result.success();
    }

    @PostMapping("/list")
    @ApiOperation("分页查询")
    public PageResult<FindNoticePageListRspVO> list(@RequestBody @Validated FindNoticePageListReqVO reqVO) {
        PageInfo<FindNoticePageListRspVO> pageInfo = adminNoticeService.list(reqVO);
        return PageResult.success(pageInfo);
    }


}
