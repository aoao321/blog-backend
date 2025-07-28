package com.aoao.blog.web.service.impl;

import com.aoao.blog.common.domain.dos.ArticleDO;
import com.aoao.blog.common.domain.dos.TagDO;
import com.aoao.blog.common.domain.mapper.ArticleMapper;
import com.aoao.blog.common.domain.mapper.TagMapper;
import com.aoao.blog.common.model.front.vo.tag.FindArticleWithTagReqVO;
import com.aoao.blog.common.model.front.vo.tag.FindTagArticlePageListRspVO;
import com.aoao.blog.common.model.front.vo.tag.FindTagListRspVO;
import com.aoao.blog.web.service.TagService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author aoao
 * @create 2025-07-25-11:17
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<FindTagListRspVO> list() {
        List<TagDO> tags = tagMapper.selectList(null);
        List<FindTagListRspVO> vos = tags.stream().map(tag -> {
            FindTagListRspVO findTagListRspVO = new FindTagListRspVO();
            BeanUtils.copyProperties(tag, findTagListRspVO);
            return findTagListRspVO;
        }).collect(Collectors.toList());
        // 统计标签下的文章数量 {id=7,count(*)=2}
        List<Map<String,Object>> tagMapList = tagMapper.selectWithArticleTotal();
        HashMap<Long, Long> articleTotalMap = new HashMap<>();
        for (Map<String, Object> map : tagMapList) {
            Long id = ((Number) map.get("id")).longValue();
            Long articleTotal = ((Long) map.get("articleTotal")).longValue();
            articleTotalMap.put(id, articleTotal);
        }
        // 合并文章数量到 VO
        for (FindTagListRspVO vo : vos) {
            Long total = articleTotalMap.getOrDefault(vo.getId(), 0L);
            vo.setArticlesTotal(total);
        }
        return vos;
    }

    @Override
    public PageInfo<FindTagArticlePageListRspVO> findArticlePage(FindArticleWithTagReqVO reqVO) {
        PageHelper.startPage(reqVO.getCurrent(), reqVO.getSize());
        // 查询标签下的文章
        List<ArticleDO> articleDOS = articleMapper.selectByTag(reqVO.getId());
        // 封装vo
        List<FindTagArticlePageListRspVO> vos = articleDOS.stream().map(articleDO -> {
            FindTagArticlePageListRspVO vo = new FindTagArticlePageListRspVO();
            BeanUtils.copyProperties(articleDO, vo);
            vo.setCreateDate(articleDO.getCreateTime().toLocalDate());
            return vo;
        }).collect(Collectors.toList());
        PageInfo<FindTagArticlePageListRspVO> pageInfo = new PageInfo<>(vos);
        return pageInfo;
    }
}
