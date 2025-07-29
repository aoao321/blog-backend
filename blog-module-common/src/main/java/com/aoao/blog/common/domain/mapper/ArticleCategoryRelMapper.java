package com.aoao.blog.common.domain.mapper;

import com.aoao.blog.common.domain.dos.ArticleCategoryRelDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

public interface ArticleCategoryRelMapper extends BaseMapper<ArticleCategoryRelDO> {

    @Select("SELECT * FROM t_article_category_rel WHERE article_id=#{articleId}")
    ArticleCategoryRelDO selectByArticleId(Long articleId);
}
