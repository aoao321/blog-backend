package com.aoao.blog.common.domain.mapper;

import com.aoao.blog.common.domain.dos.UserDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author aoao
 * @create 2025-07-13-16:26
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {
}
