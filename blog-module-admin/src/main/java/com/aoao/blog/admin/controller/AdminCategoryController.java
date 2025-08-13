package com.aoao.blog.admin.controller;

import com.aoao.blog.common.model.admin.vo.category.*;
import com.aoao.blog.admin.service.AdminCategoryService;
import com.aoao.blog.common.utils.PageResult;
import com.aoao.blog.common.utils.Result;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author aoao
 * @create 2025-07-21-19:09
 */
@RestController
@RequestMapping("/admin/category")
@Api(tags = "Admin 分类模块")
public class AdminCategoryController {

    @Autowired
    private AdminCategoryService adminCategoryService;

    @PostMapping("/add")
    @ApiOperation(value = "添加分类")
    public Result addCategory(@RequestBody @Validated AddCategoryReqVO addCategoryReqVO) {
        adminCategoryService.addCategory(addCategoryReqVO);
        return Result.success();
    }

    @PostMapping("/list")
    @ApiOperation(value = "分页查询类型")
    public PageResult<FindCategoryPageListRspVO> listCategory(@RequestBody @Validated FindCategoryPageListReqVO findCategoryPageListReqVO) {
        PageInfo<FindCategoryPageListRspVO> pageInfo = adminCategoryService.page(findCategoryPageListReqVO);
        return PageResult.success(pageInfo);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除类型")
    public Result deleteCategory(@RequestBody @Validated DeleteCategoryReqVO deleteCategoryReqVO) {
        adminCategoryService.deleteCategory(deleteCategoryReqVO);
        return Result.success();
    }

    @PostMapping("/select/list")
    @ApiOperation(value = "选择分类列表")
    public Result<List<SelectCategoryListRspVO>> selectCategoryList(){
        List<SelectCategoryListRspVO> vos = adminCategoryService.selectList();
        return Result.success(vos);
    }


}
