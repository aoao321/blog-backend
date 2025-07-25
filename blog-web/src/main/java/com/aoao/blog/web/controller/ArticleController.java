package com.aoao.blog.web.controller;

import com.aoao.blog.common.model.BasePageQuery;
import com.aoao.blog.common.model.front.vo.article.FindIndexArticlePageListRspVO;
import com.aoao.blog.common.utils.PageResult;
import com.aoao.blog.web.service.ArticleService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author aoao
 * @create 2025-07-24-13:44
 */
@RestController
@RequestMapping("/article")
@Api("前台文章")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @ApiOperation("分页查询文章")
    @PostMapping("/list")
    public PageResult<FindIndexArticlePageListRspVO> findArticlePageList(@RequestBody BasePageQuery query) {
        PageInfo<FindIndexArticlePageListRspVO> pageInfo = articleService.page(query);
        return PageResult.success(pageInfo);
    }
}
