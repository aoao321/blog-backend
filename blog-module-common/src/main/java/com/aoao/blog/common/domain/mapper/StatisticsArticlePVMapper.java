package com.aoao.blog.common.domain.mapper;

import com.aoao.blog.common.domain.dos.StatisticsArticlePVDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * @author aoao
 * @create 2025-08-01-22:08
 */
@Mapper
public interface StatisticsArticlePVMapper extends BaseMapper<StatisticsArticlePVDO> {
    List<StatisticsArticlePVDO> selectPVCount(@Param("dateList") List<LocalDate> dateList);
}
