package com.aoao.blog.web.task;

import com.aoao.blog.common.constant.RedisConstant;
import com.aoao.blog.common.domain.dos.StatisticsArticlePVDO;
import com.aoao.blog.common.domain.mapper.ArticleMapper;
import com.aoao.blog.common.domain.mapper.StatisticsArticlePVMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ViewCountSyncTask {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private StatisticsArticlePVMapper statisticsArticlePVMapper;

    private String key = RedisConstant.CACHE_ARTICLE_VIEW_NUM_KEY;

    @Scheduled(cron = "0 0/10 * * * ?") // 每 10 分钟执行一次
    public void syncViewCountToDb() {
        Map<Object, Object> viewCountMap = stringRedisTemplate.opsForHash().entries(key);
        if (viewCountMap == null || viewCountMap.isEmpty()) {
            log.info("文章访问量为空，无需同步");
            return;
        }
        for (Map.Entry<Object, Object> entry : viewCountMap.entrySet()) {
            try {
                Long articleId = Long.valueOf(entry.getKey().toString());
                Long viewCount = Long.valueOf(entry.getValue().toString());
                articleMapper.updateViewCount(articleId, viewCount);
            } catch (Exception e) {
                log.error("同步文章 [{}] 阅读量失败", entry.getKey(), e);
            }
        }

        log.info("同步文章阅读量完成，共 {} 条", viewCountMap.size());
    }

    @Scheduled(cron = "0 25 10 * * ?")
    public void syncViewCountTOPVTable() {
        // 获取当天pv
        LocalDate localDate = LocalDate.now();
        List<Object> values = stringRedisTemplate.opsForHash().values(key);
        long count = 0;
        for (Object value : values) {
            long view = Long.valueOf(value.toString());
            count += view;
        }
        // 获得上一天的pv
        LocalDate yesterday = localDate.minusDays(1);
        StatisticsArticlePVDO yesterPVDO = statisticsArticlePVMapper.selectOne(new QueryWrapper<StatisticsArticlePVDO>()
                .eq("pv_date", yesterday));
       Long yesterPV = (yesterPVDO == null || yesterPVDO.getPvCount() == null)
               ? 0L
               : yesterPVDO.getPvCount();

        StatisticsArticlePVDO statisticsArticlePVDO = new StatisticsArticlePVDO();
        statisticsArticlePVDO.setPvDate(localDate);
        // 上一天pv等于null
        if(yesterPV == null){
            statisticsArticlePVDO.setPvCount(count);
        }else {
            statisticsArticlePVDO.setPvCount(count-yesterPV);
        }
        statisticsArticlePVMapper.insert(statisticsArticlePVDO);
    }
}
