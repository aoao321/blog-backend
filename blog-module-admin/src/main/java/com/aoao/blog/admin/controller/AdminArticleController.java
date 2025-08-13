package com.aoao.blog.admin.controller;

import com.aoao.blog.admin.service.AdminArticleService;
import com.aoao.blog.common.model.admin.vo.article.*;
import com.aoao.blog.common.utils.PageResult;
import com.aoao.blog.common.utils.Result;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author aoao
 * @create 2025-07-22-16:01
 */
@RestController
@RequestMapping("/admin/article")
public class AdminArticleController {

    @Autowired
    private AdminArticleService adminArticleService;

    @PostMapping("/publish")
    @ApiOperation(value = "文章发布")
    public Result publishArticle(@RequestBody @Validated PublishArticleReqVO publishArticleReqVO) {
        adminArticleService.publishArticle(publishArticleReqVO);
        return Result.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "文章删除")
    public Result deleteArticle(@RequestBody @Validated DeleteArticleReqVO deleteArticleReqVO) {
        adminArticleService.deleteArticle(deleteArticleReqVO);
        return Result.success();
    }

    @PostMapping("/list")
    @ApiOperation(value = "查询文章分页数据")
    public PageResult<FindArticlePageListRspVO> findArticlePageList(@RequestBody @Validated FindArticlePageListReqVO findArticlePageListReqVO) {
        PageInfo<FindArticlePageListRspVO> pageInfo = adminArticleService.findArticlePageList(findArticlePageListReqVO);
        return PageResult.success(pageInfo);
    }

    @PostMapping("/detail")
    @ApiOperation(value = "查询文章详情")
    public Result<FindArticleDetailRspVO> findArticleDetail(@RequestBody @Validated FindArticleDetailReqVO findArticleDetailReqVO) {
        FindArticleDetailRspVO rspVO = adminArticleService.showDetail(findArticleDetailReqVO);
        return Result.success(rspVO);
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新文章")
    public Result updateArticle(@RequestBody @Validated UpdateArticleReqVO updateArticleReqVO) {
        adminArticleService.updateArticle(updateArticleReqVO);
        return Result.success();
    }

    @PostMapping("/isTop/update")
    @ApiOperation(value = "置顶")
    public Result isTopUpdate(@RequestBody @Validated IsTopUpdateArticleReqVO isTopUpdateArticleReqVO) {
        adminArticleService.isTopUpdate(isTopUpdateArticleReqVO);
        return Result.success();
    }

    @PostMapping("/isPublish/update")
    @ApiOperation(value = "修改发布")
    public Result isPublish(@RequestBody @Validated IsPublishUpdateArticleReqVO isPublishUpdateArticleReqVO) {
        adminArticleService.isPublishUpdate(isPublishUpdateArticleReqVO);
        return Result.success();
    }


}
