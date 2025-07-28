package com.aoao.blog.web.service;

import com.aoao.blog.common.model.front.vo.tag.FindArticleWithTagReqVO;
import com.aoao.blog.common.model.front.vo.tag.FindTagArticlePageListRspVO;
import com.aoao.blog.common.model.front.vo.tag.FindTagListRspVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author aoao
 * @create 2025-07-25-11:17
 */
public interface TagService {
    List<FindTagListRspVO> list();

    PageInfo<FindTagArticlePageListRspVO> findArticlePage(FindArticleWithTagReqVO reqVO);
}
