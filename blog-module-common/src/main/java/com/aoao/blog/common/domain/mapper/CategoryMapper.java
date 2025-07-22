package com.aoao.blog.common.domain.mapper;

import com.aoao.blog.common.domain.dos.CategoryDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

import java.util.Locale;


/**
 * @author aoao
 * @create 2025-07-21-19:25
 */
@Mapper
public interface CategoryMapper extends BaseMapper<CategoryDO> {
}
