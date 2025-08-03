package com.aoao.blog.web.event.subscriber;


import com.aoao.blog.common.constant.RedisConstant;
import com.aoao.blog.common.domain.mapper.ArticleMapper;
import com.aoao.blog.web.event.ReadArticleEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.aoao.blog.common.constant.RedisConstant.CACHE_ARTICLE_VIEW_NUM_KEY;

@Component
@Slf4j
public class ReadArticleSubscriber implements ApplicationListener<ReadArticleEvent> {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private String viewNumKey = CACHE_ARTICLE_VIEW_NUM_KEY;

    private String PVKey = RedisConstant.PV_KEY;

    @Override
    @Async("threadPoolTaskExecutor")
    public void onApplicationEvent(ReadArticleEvent event) {
        // 在这里处理收到的事件，可以是任何逻辑操作
        Long articleId = event.getArticleId();
        // 获取当前线程名称
        String threadName = Thread.currentThread().getName();
        log.info("==> threadName: {}", threadName);
        log.info("==> 文章阅读事件消费成功，articleId: {}", articleId);

        // 文章访问量+1
        stringRedisTemplate.opsForHash().increment(viewNumKey, articleId.toString(), 1);

        // 今天pv+1
        LocalDate today = LocalDate.now();
        stringRedisTemplate.opsForHash().increment(PVKey, today.toString(), 1);

    }
}
