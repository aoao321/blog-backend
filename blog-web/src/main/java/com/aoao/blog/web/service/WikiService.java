package com.aoao.blog.web.service;

import com.aoao.blog.common.model.front.vo.wiki.*;

import java.util.List;

/**
 * @author aoao
 * @create 2025-08-12-12:34
 */
public interface WikiService {
    List<FindWikiListRspVO> list();

    List<FindWikiCatalogListRspVO> catalogList(FindWikiCatalogListReqVO findWikiCatalogListReqVO);

    FindWikiArticlePreNextRspVO preNext(FindWikiArticlePreNextReqVO findWikiArticlePreNextReqVO);
}
