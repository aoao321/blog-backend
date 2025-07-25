package com.aoao.blog.web.service;

import com.aoao.blog.common.model.front.vo.article.FindArchiveArticlePageListReqVO;
import com.aoao.blog.common.model.front.vo.article.FindArchiveArticlePageListRspVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author aoao
 * @create 2025-07-25-13:27
 */
public interface ArchiveService {
    PageInfo<FindArchiveArticlePageListRspVO> page(FindArchiveArticlePageListReqVO reqVO);
}
