package com.aoao.blog.web.controller;

import com.aoao.blog.common.model.front.vo.wiki.*;
import com.aoao.blog.common.utils.Result;
import com.aoao.blog.web.service.WikiService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author aoao
 * @create 2025-08-12-12:33
 */
@RestController
@RequestMapping("/wiki")
public class WikiController {

    @Autowired
    private WikiService wikiService;

    @PostMapping("/list")
    @ApiOperation("查询")
    public Result<List<FindWikiListRspVO>> list() {
        List<FindWikiListRspVO> rsp = wikiService.list();
        return Result.success(rsp);
    }

    @PostMapping("/catalog/list")
    @ApiOperation("目录查询")
    public Result<List<FindWikiCatalogListRspVO>> catalogList(@RequestBody @Validated FindWikiCatalogListReqVO findWikiCatalogListReqVO) {
        List<FindWikiCatalogListRspVO> resp = wikiService.catalogList(findWikiCatalogListReqVO);
        return Result.success(resp);
    }

    @PostMapping("/article/preNext")
    @ApiOperation(value = "获取知识库文章上下页")
    public Result<FindWikiArticlePreNextRspVO> preNext(@RequestBody @Validated FindWikiArticlePreNextReqVO findWikiArticlePreNextReqVO) {
        FindWikiArticlePreNextRspVO rspVO = wikiService.preNext(findWikiArticlePreNextReqVO);
        return Result.success(rspVO);
    }


}
