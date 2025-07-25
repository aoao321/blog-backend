package com.aoao.blog.web.controller;

import com.aoao.blog.common.model.front.vo.article.FindArchiveArticlePageListReqVO;
import com.aoao.blog.common.model.front.vo.article.FindArchiveArticlePageListRspVO;
import com.aoao.blog.common.utils.PageResult;
import com.aoao.blog.web.service.ArchiveService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author aoao
 * @create 2025-07-25-13:26
 */
@RestController
@RequestMapping("/archive")
@ApiOperation("归档模块")
public class ArchiveController {

    @Autowired
    private ArchiveService archiveService;

    @PostMapping("/list")
    @ApiOperation("归档列表")
    public PageResult<FindArchiveArticlePageListRspVO> list(@RequestBody FindArchiveArticlePageListReqVO reqVO) {
       PageInfo<FindArchiveArticlePageListRspVO> pageInfo = archiveService.page(reqVO);
       return PageResult.success(pageInfo);
    }
}
