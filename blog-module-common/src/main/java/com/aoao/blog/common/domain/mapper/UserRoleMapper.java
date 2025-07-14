package com.aoao.blog.common.domain.mapper;

import com.aoao.blog.common.domain.dos.UserRoleDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author aoao
 * @create 2025-07-14-14:33
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRoleDO> {

    @Select("SELECT role FROM t_user_role WHERE username = #{username}")
    List<String> selectRoleNameByUserName(@Param("username") String username);
}
