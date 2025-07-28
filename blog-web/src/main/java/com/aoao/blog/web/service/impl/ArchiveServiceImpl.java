package com.aoao.blog.web.service.impl;

import com.aoao.blog.common.domain.dos.ArticleDO;
import com.aoao.blog.common.domain.mapper.ArticleMapper;
import com.aoao.blog.common.model.front.vo.article.FindArchiveArticlePageListReqVO;
import com.aoao.blog.common.model.front.vo.article.FindArchiveArticlePageListRspVO;
import com.aoao.blog.common.model.front.vo.article.FindArchiveArticleRspVO;
import com.aoao.blog.web.service.ArchiveService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author aoao
 * @create 2025-07-25-13:28
 */
@Service
public class ArchiveServiceImpl implements ArchiveService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public PageInfo<FindArchiveArticlePageListRspVO> page(FindArchiveArticlePageListReqVO reqVO) {
        // 分页
        PageHelper.startPage(reqVO.getCurrent(), reqVO.getSize());
        // 查询
        List<ArticleDO> articleDOS = articleMapper.selectList(null);
        // 转换成vo
        List<FindArchiveArticleRspVO> vos = articleDOS.stream().map(articleDO -> {
            FindArchiveArticleRspVO vo = new FindArchiveArticleRspVO();
            BeanUtils.copyProperties(articleDO, vo);
            // 设置发布日期和月份
            vo.setCreateDate(articleDO.getCreateTime().toLocalDate());
            vo.setCreateMonth(YearMonth.from(vo.getCreateDate()));
            return vo;
        }).collect(Collectors.toList());

        // 封装成FindArchiveArticlePageListRspVO {2025-03 : List }
        Map<YearMonth,List<FindArchiveArticleRspVO>> map = new HashMap<>();
        for (FindArchiveArticleRspVO vo : vos) {
           // 如果FindArchiveArticlePageListRspVO已经存在当前年月，就直接articles.add(),如果没有就new ArrayList()
            map.computeIfAbsent(vo.getCreateMonth(), k -> new ArrayList<>()).add(vo);
        }

        // 转换成 List<FindArchiveArticlePageListRspVO>
        List<FindArchiveArticlePageListRspVO> result = map.entrySet().stream().map(entry -> {
            FindArchiveArticlePageListRspVO groupVO = new FindArchiveArticlePageListRspVO();
            groupVO.setMonth(entry.getKey());
            groupVO.setArticles(entry.getValue());
            return groupVO;
        }).collect(Collectors.toList());

        PageInfo<FindArchiveArticlePageListRspVO> pageInfo = new PageInfo<>(result);
        return pageInfo;
    }
}
