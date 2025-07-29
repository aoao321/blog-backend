package com.aoao.blog.common.domain.mapper;


import com.aoao.blog.common.domain.dos.ArticleDO;
import com.aoao.blog.common.model.admin.vo.article.FindArticleDetailRspVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


public interface ArticleMapper extends BaseMapper<ArticleDO> {

    FindArticleDetailRspVO selectDetail(Long articleId);

    @Select("SELECT a.* FROM t_article a LEFT JOIN t_article_category_rel ac ON a.id = ac.article_id WHERE ac.category_id = #{categoryId} AND a.is_deleted=0")
    List<ArticleDO> selectByCategory(Long categoryId);

    @Select("SELECT a.* FROM t_article a LEFT JOIN t_article_tag_rel atr ON a.id = atr.article_id WHERE atr.tag_id = #{tagId} AND a.is_deleted=0")
    List<ArticleDO> selectByTag(Long tagId);

    @Update("Update t_article SET read_num = read_num+1 WHERE id=#{aricleId}")
    void increaseReadNum(Long articleId);

    @Update("UPDATE t_article SET read_num = #{readView} WHERE id = #{id}")
    void updateViewCount(@Param("id") Long articleId,@Param("readView") Long viewCount);
}
