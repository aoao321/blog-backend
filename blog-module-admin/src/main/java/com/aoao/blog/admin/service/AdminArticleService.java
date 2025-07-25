package com.aoao.blog.admin.service;

import com.aoao.blog.common.model.admin.vo.article.*;
import com.github.pagehelper.PageInfo;

/**
 * @author aoao
 * @create 2025-07-22-16:02
 */
public interface AdminArticleService {
    void publishArticle(PublishArticleReqVO publishArticleReqVO);

    void deleteArticle(DeleteArticleReqVO deleteArticleReqVO);

    PageInfo<FindArticlePageListRspVO> findArticlePageList(FindArticlePageListReqVO findArticlePageListReqVO);

    FindArticleDetailRspVO showDetail(FindArticleDetailReqVO findArticleDetailReqVO);

    void updateArticle(UpdateArticleReqVO updateArticleReqVO);
}
