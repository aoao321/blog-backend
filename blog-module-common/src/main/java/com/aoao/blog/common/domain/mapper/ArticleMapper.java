package com.aoao.blog.common.domain.mapper;


import com.aoao.blog.common.domain.dos.ArticleDO;
import com.aoao.blog.common.model.admin.vo.article.FindArticleDetailRspVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;



public interface ArticleMapper extends BaseMapper<ArticleDO> {

    FindArticleDetailRspVO selectDetail(Long articleId);
}
