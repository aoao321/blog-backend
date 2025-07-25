package com.aoao.blog.common.domain.mapper;

import com.aoao.blog.common.domain.dos.TagDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author aoao
 * @create 2025-07-22-9:39
 */
@Mapper
public interface TagMapper extends BaseMapper<TagDO> {
    List<TagDO> selectExist(@Param("tags") List<String> tags);

    void insertBatch(@Param("list") List<TagDO> tagList);

    List<Long> selectByArticleId(Long articleId);

    List<Map<String, Object>> selectBatchByArticleIds(@Param("aIds") List<Long> articleIds);
}
