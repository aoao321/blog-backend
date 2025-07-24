package com.aoao.blog.common.domain.mapper;

import com.aoao.blog.common.domain.dos.ArticleTagRelDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleTagRelMapper extends BaseMapper<ArticleTagRelDO> {

    void insertBatch(@Param("list") List<ArticleTagRelDO> articleTagRelDOList);
}
