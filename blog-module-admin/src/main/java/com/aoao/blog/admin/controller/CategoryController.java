package com.aoao.blog.admin.controller;

import com.aoao.blog.admin.model.vo.category.AddCategoryReqVO;
import com.aoao.blog.admin.model.vo.category.DeleteCategoryReqVO;
import com.aoao.blog.admin.model.vo.category.FindCategoryPageListReqVO;
import com.aoao.blog.admin.model.vo.category.FindCategoryPageListRspVO;
import com.aoao.blog.admin.service.CategoryService;
import com.aoao.blog.common.domain.dos.CategoryDO;
import com.aoao.blog.common.utils.PageResult;
import com.aoao.blog.common.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author aoao
 * @create 2025-07-21-19:09
 */
@RestController
@RequestMapping("/admin/category")
@Api(tags = "Admin 分类模块")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/add")
    @ApiOperation(value = "添加分类")
    public Result addCategory(@RequestBody @Valid AddCategoryReqVO addCategoryReqVO) {
        categoryService.addCategory(addCategoryReqVO);
        return Result.success();
    }

    @PostMapping("/list")
    @ApiOperation(value = "分页查询类型")
    public PageResult<FindCategoryPageListRspVO> listCategory(@RequestBody @Valid FindCategoryPageListReqVO findCategoryPageListReqVO) {
        PageResult<FindCategoryPageListRspVO> page = categoryService.page(findCategoryPageListReqVO);
        return page;
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除类型")
    public Result deleteCategory(@RequestBody @Valid DeleteCategoryReqVO deleteCategoryReqVO) {
        categoryService.deleteCategory(deleteCategoryReqVO);
        return Result.success();
    }


}
