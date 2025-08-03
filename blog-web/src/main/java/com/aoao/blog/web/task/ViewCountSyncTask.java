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
import java.util.Locale;
import java.util.Map;

import static com.aoao.blog.common.constant.RedisConstant.CACHE_ARTICLE_VIEW_NUM_KEY;

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

    private String viewNumKey = CACHE_ARTICLE_VIEW_NUM_KEY;
    private String PVKey = RedisConstant.PV_KEY;

    @Scheduled(cron = "0 0/30 * * * ?") // 每 30 分钟执行一次
    public void syncViewCountToDb() {
        Map<Object, Object> viewCountMap = stringRedisTemplate.opsForHash().entries(viewNumKey);
        if (viewCountMap == null || viewCountMap.isEmpty()) {
            log.info("文章访问量为空，无需同步");
            return;
        }
        for (Map.Entry<Object, Object> entry : viewCountMap.entrySet()) {
            try {
                Long articleId = Long.valueOf(entry.getKey().toString());
                Long viewIncr = Long.valueOf(entry.getValue().toString());
                articleMapper.incrViewCount(articleId, viewIncr);
            } catch (Exception e) {
                log.error("同步文章 [{}] 阅读量失败", entry.getKey(), e);
            }finally {
                // 清空 Redis 计数器
                stringRedisTemplate.delete(viewNumKey);
            }
        }

        log.info("同步文章阅读量完成，共 {} 条", viewCountMap.size());
    }

    @Scheduled(cron = "0 30 11 * * ?")
    public void syncViewCountTOPVTable() {
        // 获取到今天的pv插入数据库中
        LocalDate today = LocalDate.now();
        String pvStr = (String)stringRedisTemplate.opsForHash().get(PVKey, today.toString());
        Long pv = Long.valueOf(pvStr);
        StatisticsArticlePVDO statisticsArticlePVDO = new StatisticsArticlePVDO();
        statisticsArticlePVDO.setPvDate(today);
        statisticsArticlePVDO.setPvCount(pv);
        // 插入表中
        statisticsArticlePVMapper.insert(statisticsArticlePVDO);
        // 删除昨天的记录
        LocalDate yesterday = today.plusDays(1);
        stringRedisTemplate.opsForHash().delete(PVKey, yesterday.toString());
    }
}
