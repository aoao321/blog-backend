package com.aoao.blog.common.domain.mapper;

import com.aoao.blog.common.domain.dos.CategoryDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * @author aoao
 * @create 2025-07-21-19:25
 */
@Mapper
public interface CategoryMapper extends BaseMapper<CategoryDO> {
    List<Map<String, Object>> selectBatchByArticleIds(@Param("aIds") List<Long> articleIds);

    List<Map<String, Object>> selectWithArticleTotal();

}
