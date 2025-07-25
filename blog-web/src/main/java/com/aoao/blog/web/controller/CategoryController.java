package com.aoao.blog.web.controller;

import com.aoao.blog.common.model.front.vo.category.FindCategoryListRspVO;
import com.aoao.blog.common.model.front.vo.tag.FindTagListRspVO;
import com.aoao.blog.common.utils.Result;
import com.aoao.blog.web.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author aoao
 * @create 2025-07-25-12:28
 */
@RestController
@RequestMapping("/category")
@Api("前台分类")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation("查询分类集合")
    @PostMapping("/list")
    public Result<List<FindCategoryListRspVO>> findTagList() {
        List<FindCategoryListRspVO> vos = categoryService.list();
        return Result.success(vos);
    }
}
