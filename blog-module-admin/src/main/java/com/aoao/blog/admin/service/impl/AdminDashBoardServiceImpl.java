package com.aoao.blog.admin.service.impl;

import com.aoao.blog.admin.service.AdminDashBoardService;
import com.aoao.blog.common.domain.dos.ArticleDO;
import com.aoao.blog.common.domain.dos.ArticlePublishCountDO;
import com.aoao.blog.common.domain.mapper.ArticleMapper;
import com.aoao.blog.common.domain.mapper.CategoryMapper;
import com.aoao.blog.common.domain.mapper.TagMapper;
import com.aoao.blog.common.model.admin.vo.dashboard.FindDashboardStatisticsInfoRspVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;

/**
 * @author aoao
 * @create 2025-08-01-17:28
 */
@Service
public class AdminDashBoardServiceImpl implements AdminDashBoardService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;

    @Override
    public FindDashboardStatisticsInfoRspVO statistics() {
// 查询文章总数
        Long articleTotalCount = articleMapper.selectCount(Wrappers.emptyWrapper());

        // 查询分类总数
        Long categoryTotalCount = categoryMapper.selectCount(Wrappers.emptyWrapper());

        // 查询标签总数
        Long tagTotalCount = tagMapper.selectCount(Wrappers.emptyWrapper());

        // 总浏览量
        List<ArticleDO> articleDOS = articleMapper.selectList(Wrappers.emptyWrapper());
        Long pvTotalCount = 0L;

        if (!CollectionUtils.isEmpty(articleDOS)) {
            // 所有 read_num 相加
            pvTotalCount = articleDOS.stream().mapToLong(ArticleDO::getReadNum).sum();
        }

        // 组装 VO 类
        FindDashboardStatisticsInfoRspVO vo = FindDashboardStatisticsInfoRspVO.builder()
                .articleTotalCount(articleTotalCount)
                .categoryTotalCount(categoryTotalCount)
                .tagTotalCount(tagTotalCount)
                .pvTotalCount(pvTotalCount)
                .build();

        return vo;

    }

    @Override
    public Map<LocalDate, Long> publishArticleStatistics() {
        // 获取现在日期
        LocalDate today = LocalDate.now();
        LocalDate passday = today.minusMonths(6);
        // 查询半年内出所有文章 {[date:xx,count:xx],[...]  }
        List<ArticlePublishCountDO> dos = articleMapper.selectCountHalfaYear(today, passday);
        // 根据日期分类
        Map<LocalDate, Long> map = new HashMap<>();
        for (ArticlePublishCountDO aDo : dos) {
            LocalDate date = aDo.getDate();
            Long count = aDo.getCount();
            map.put(date,count);
        }

        return map;


    }
}
