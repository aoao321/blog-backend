package com.aoao.blog.admin.controller;

import com.aoao.blog.common.model.admin.vo.tag.*;
import com.aoao.blog.admin.service.TagService;
import com.aoao.blog.common.utils.PageResult;
import com.aoao.blog.common.utils.Result;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author aoao
 * @create 2025-07-21-21:50
 */
@RestController
@RequestMapping("/admin/tag")
@Api(tags = "Admin 标签模块")
public class TagController {

    @Autowired
    private TagService tagService;

    @PostMapping("/add")
    @ApiOperation("新增标签")
    public Result addTag(@RequestBody @Valid AddTagReqVO addTagReqVO) {
        tagService.addTag(addTagReqVO);
        return Result.success();
    }

    @PostMapping("/delete")
    @ApiOperation("删除标签")
    public Result deleteTag(@RequestBody @Valid DeleteTagReqVO deleteTagReqVO) {
        tagService.deleteTag(deleteTagReqVO);
        return Result.success();
    }

    @PostMapping("/list")
    @ApiOperation("分页查询")
    public PageResult<FindTagPageListRspVO> listTag(@RequestBody @Valid FindTagPageListReqVO findTagPageListReqVO) {
        PageInfo<FindTagPageListRspVO> pageInfo = tagService.page(findTagPageListReqVO);
        return PageResult.success(pageInfo);
    }

    @PostMapping("/select/list")
    @ApiOperation("选择标签列表")
    public Result<List<SelectTagListRspVO>> selectTagList(){
        List<SelectTagListRspVO> rspVOS = tagService.selectTagList();
        return Result.success(rspVOS);
    }

}
