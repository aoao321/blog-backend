package com.aoao.blog.admin.controller;

import com.aoao.blog.admin.service.AdminWikiService;
import com.aoao.blog.common.model.admin.vo.wiki.*;
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

import javax.validation.Valid;
import java.util.List;

/**
 * @author aoao
 * @create 2025-08-08-13:39
 */
@RestController
@RequestMapping("/admin/wiki")
public class AdminWikiController {

    @Autowired
    private AdminWikiService adminWikiService;

    @PostMapping("/add")
    @ApiOperation("新增知识库")
    public Result add(@RequestBody @Validated AddWikiReqVO addWikiReqVO) {
        adminWikiService.add(addWikiReqVO);
        return Result.success();
    }

    @PostMapping("/delete")
    @ApiOperation("删除知识库")
    public Result delete(@RequestBody @Validated DeleteWikiReqVO deleteWikiReqVO) {
        adminWikiService.delete(deleteWikiReqVO);
        return Result.success();
    }

    @PostMapping("/list")
    @ApiOperation("分页查询知识库")
    public PageResult<FindWikiPageListRspVO> page(@RequestBody @Validated FindWikiPageListReqVO findWikiPageListReqVO) {
        PageInfo<FindWikiPageListRspVO> pageInfo = adminWikiService.page(findWikiPageListReqVO);
        return PageResult.success(pageInfo);
    }

    @PostMapping("/isTop/update")
    @ApiOperation("置顶")
    public Result top(@RequestBody @Validated UpdateWikiIsTopReqVO updateWikiIsTopReqVO) {
        adminWikiService.top(updateWikiIsTopReqVO);
        return Result.success();
    }

    @PostMapping("/isPublish/update")
    @ApiOperation("发布")
    public Result publish(@RequestBody @Validated UpdateWikiIsPublishReqVO updateWikiIsPublishReqVO) {
        adminWikiService.publish(updateWikiIsPublishReqVO);
        return Result.success();
    }

    @PostMapping("/update")
    @ApiOperation("修改知识库")
    public Result update(@RequestBody @Validated UpdateWikiReqVO updateWikiReqVO) {
        adminWikiService.update(updateWikiReqVO);
        return Result.success();
    }

    @PostMapping("/catalog/list")
    @ApiOperation("查询知识库详情")
    public Result<List<FindWikiCatalogListRspVO>> findCatalogList(@RequestBody @Validated FindWikiCatalogListReqVO findWikiCatalogListReqVO) {
        List<FindWikiCatalogListRspVO> vo = adminWikiService.findCatalogList(findWikiCatalogListReqVO);
        return Result.success(vo);
    }

    @PostMapping("/catalog/update")
    @ApiOperation("修改知识库目录")
    public Result updateCatalog(@RequestBody @Validated UpdateWikiCatalogReqVO updateWikiCatalogReqVO) {
        adminWikiService.updateCatalog(updateWikiCatalogReqVO);
        return Result.success();
    }

}
