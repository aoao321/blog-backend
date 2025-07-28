package com.aoao.blog.web.controller;

import com.aoao.blog.common.model.BasePageQuery;
import com.aoao.blog.common.model.front.vo.article.FindIndexArticlePageListRspVO;
import com.aoao.blog.common.model.front.vo.tag.FindArticleWithTagReqVO;
import com.aoao.blog.common.model.front.vo.tag.FindTagArticlePageListRspVO;
import com.aoao.blog.common.model.front.vo.tag.FindTagListRspVO;
import com.aoao.blog.common.utils.PageResult;
import com.aoao.blog.common.utils.Result;
import com.aoao.blog.web.service.TagService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author aoao
 * @create 2025-07-25-11:13
 */
@RestController
@RequestMapping("/tag")
@Api("前台标签")
public class TagController {

    @Autowired
    private TagService tagService;

    @ApiOperation("查询标签集合")
    @PostMapping("/list")
    public Result<List<FindTagListRspVO>> findTagList() {
        List<FindTagListRspVO> vos = tagService.list();
        return Result.success(vos);
    }

    @ApiOperation("查询标签下文章列表")
    @PostMapping("/article/list")
    public PageResult<FindTagArticlePageListRspVO> findTagArticleList(@RequestBody FindArticleWithTagReqVO reqVO) {
        PageInfo<FindTagArticlePageListRspVO> pageInfo = tagService.findArticlePage(reqVO);
        return PageResult.success(pageInfo);
    }
}
