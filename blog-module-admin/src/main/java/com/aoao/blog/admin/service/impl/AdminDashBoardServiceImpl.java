package com.aoao.blog.admin.service.impl;

import com.aoao.blog.admin.service.AdminDashBoardService;
import com.aoao.blog.common.domain.dos.*;
import com.aoao.blog.common.domain.mapper.*;
import com.aoao.blog.common.model.admin.vo.dashboard.FindDashboardStatisticsCategoryRspVO;
import com.aoao.blog.common.model.admin.vo.dashboard.FindDashboardPVRspVO;
import com.aoao.blog.common.model.admin.vo.dashboard.FindDashboardStatisticsInfoRspVO;
import com.aoao.blog.common.model.admin.vo.dashboard.FindDashboardStatisticsTagRspVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private StatisticsArticlePVMapper statisticsArticlePVMapper;
    @Autowired
    private ArticleTagRelMapper articleTagRelMapper;

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

    @Override
    public FindDashboardPVRspVO pvStatistics() {
        // 获取过去七天日期集合
        LocalDate today = LocalDate.now();
        LocalDate minusWeeks = today.minusWeeks(1);
        List<LocalDate> dateList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            dateList.add(minusWeeks.plusDays(i));
        }
        // 查询7天的pv
        List<StatisticsArticlePVDO> records = statisticsArticlePVMapper.selectPVCount(dateList);
        // 先转为 map，方便查找
        Map<LocalDate, Long> dbMap = records.stream()
                .collect(Collectors.toMap(
                        StatisticsArticlePVDO::getPvDate,
                        StatisticsArticlePVDO::getPvCount
                ));
        // 按传入顺序返回 count，没有的补0
        List<Long> result = new ArrayList<>();
        for (LocalDate date : dateList) {
            result.add(dbMap.getOrDefault(date, 0L));
        }
        FindDashboardPVRspVO vo = new FindDashboardPVRspVO(dateList, result);
        return vo;
    }

    @Override
    public List<FindDashboardStatisticsCategoryRspVO> categoryStatistics() {
        List<CategoryDO> categoryDOS = categoryMapper.selectList(null);
        List<FindDashboardStatisticsCategoryRspVO> vos = categoryDOS.stream().map(categoryDO -> {
            return new FindDashboardStatisticsCategoryRspVO(categoryDO.getName(), categoryDO.getId());
        }).collect(Collectors.toList());
        return vos;
    }

    @Override
    public FindDashboardStatisticsTagRspVO tagStatistics() {
        List<Object> o = tagMapper.selectObjs(new QueryWrapper<TagDO>().select("name"));
        List<String> tagNames = o.stream()
                .map(Object::toString)
                .collect(Collectors.toList());
        // 统计每个标签下有多少文章{[tag_id:1 count:1],}
        List<Map<String,Object>> mapList = articleTagRelMapper.selectArticleCountByTags(tagNames);
        Map<String, Long> result = new HashMap<>();
        // 遍历
        for (Map<String, Object> map : mapList) {
            String tagName = (String) map.get("tagName");
            Long count = (Long) map.getOrDefault("articleCount", 0L);
            result.put(tagName,count);
        }
        // 按 tagNames 顺序构建文章数量列表
        List<Long> articleCounts = new ArrayList<>();
        // {前端:1,后端:1}
        for (String tagName : tagNames) {
            articleCounts.add(result.getOrDefault(tagName, 0L));
        }

        FindDashboardStatisticsTagRspVO vo = new FindDashboardStatisticsTagRspVO(tagNames,articleCounts);
        return vo;
    }
}
