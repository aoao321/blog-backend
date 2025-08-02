package com.aoao.blog.web;

import com.aoao.blog.common.domain.dos.ArticleDO;
import com.aoao.blog.common.domain.mapper.ArticleMapper;
import com.aoao.blog.common.domain.mapper.UserMapper;
import com.aoao.blog.common.domain.mapper.UserRoleMapper;
import com.aoao.blog.common.model.front.vo.article.FindArchiveArticleRspVO;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.coyote.http11.Constants.a;

@SpringBootTest
@Slf4j
class BlogWebApplicationTests {


    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Test
    void testMyBatisPlus() {
        //userMapper.insert(new UserDO(1l,"小敖兴","1243", new Date(),new Date(),false));
        //userMapper.selectById(1l);
        System.out.println(userRoleMapper.selectRoleNameByUserName("admin"));
    }

    @Test
    void testString() {
        // 分页
        PageHelper.startPage(1,10);
        // 查询
        List<ArticleDO> articleDOS = articleMapper.selectList(null);
        // 转换成vo
        List<FindArchiveArticleRspVO> findArchiveArticleRspVOList = articleDOS.stream().map(articleDO -> {
            FindArchiveArticleRspVO vo = new FindArchiveArticleRspVO();
            BeanUtils.copyProperties(articleDO, vo);
            // 设置发布日期和月份
            vo.setCreateDate(articleDO.getCreateTime().toLocalDate());
            vo.setCreateMonth(YearMonth.from(vo.getCreateDate()));
            return vo;
        }).collect(Collectors.toList());
        System.out.println(findArchiveArticleRspVOList);
    }

    @Test
    void testRedis() {
       stringRedisTemplate.opsForValue().set("test","test");
        System.out.println(stringRedisTemplate.opsForValue().get("test"));
    }

    @Test
    void testRedis2() {

    }





}
