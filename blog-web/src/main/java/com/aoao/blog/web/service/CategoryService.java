package com.aoao.blog.web.service;

import com.aoao.blog.common.model.front.vo.category.FindArticleWithTypeCategoryReqVO;
import com.aoao.blog.common.model.front.vo.category.FindCategoryArticlePageListRspVO;
import com.aoao.blog.common.model.front.vo.category.FindCategoryListRspVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author aoao
 * @create 2025-07-25-12:29
 */
public interface CategoryService {
    List<FindCategoryListRspVO> list();

    PageInfo<FindCategoryArticlePageListRspVO> findCategoryArticlePageList(FindArticleWithTypeCategoryReqVO reqVO);
}
