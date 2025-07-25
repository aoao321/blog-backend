package com.aoao.blog.web.service;

import com.aoao.blog.common.model.BasePageQuery;
import com.aoao.blog.common.model.front.vo.article.FindIndexArticlePageListRspVO;
import com.github.pagehelper.PageInfo;

/**
 * @author aoao
 * @create 2025-07-24-14:02
 */
public interface ArticleService {
    PageInfo<FindIndexArticlePageListRspVO> page(BasePageQuery query);
}
