package com.aoao.blog.common.domain.mapper;

import com.aoao.blog.common.domain.dos.CommentDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author aoao
 * @create 2025-08-11-17:12
 */
@Mapper
public interface CommentMapper extends BaseMapper<CommentDO> {
}
