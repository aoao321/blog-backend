package com.aoao.blog.common.domain.mapper;

import com.aoao.blog.common.domain.dos.WikiDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;


public interface WikiMapper extends BaseMapper<WikiDO> {

    @Select("SELECT MAX(weight) FROM t_wiki")
    Integer selectMaxWeight();




}
