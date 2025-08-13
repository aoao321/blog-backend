package com.aoao.blog.web.service.impl;

import com.aoao.blog.common.domain.dos.WikiCatalogDO;
import com.aoao.blog.common.domain.dos.WikiDO;
import com.aoao.blog.common.domain.mapper.WikiCatalogMapper;
import com.aoao.blog.common.domain.mapper.WikiMapper;
import com.aoao.blog.common.enums.ResponseCodeEnum;
import com.aoao.blog.common.enums.WikiCatalogLevelEnum;
import com.aoao.blog.common.exception.BizException;
import com.aoao.blog.common.model.front.vo.article.FindPreNextArticleRspVO;
import com.aoao.blog.common.model.front.vo.wiki.*;
import com.aoao.blog.web.service.WikiService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author aoao
 * @create 2025-08-12-12:34
 */
@Service
public class WikiServiceImpl implements WikiService {

    @Autowired
    private WikiMapper wikiMapper;
    @Autowired
    private WikiCatalogMapper wikiCatalogMapper;

    @Override
    public List<FindWikiListRspVO> list() {
        List<WikiDO> wikiDOS = wikiMapper.selectList(new QueryWrapper<WikiDO>()
                .eq("is_publish", 1)
                .orderByDesc("weight")
                .orderByDesc("create_time")
        );
        // 转vo
        List<FindWikiListRspVO> vos = wikiDOS.stream().map(wikiDO -> {
            FindWikiListRspVO rspVO = new FindWikiListRspVO();
            BeanUtils.copyProperties(wikiDO, rspVO);
            // 置顶？
            Integer weight = wikiDO.getWeight();
            rspVO.setIsTop(false);
            if (weight != null && weight > 0) {
                rspVO.setIsTop(true);
            }
            WikiCatalogDO wikiCatalogDO = wikiCatalogMapper.selectOne(new QueryWrapper<WikiCatalogDO>()
                    .eq("wiki_id", wikiDO.getId()) // 查询指定知识库 id
                    .eq("level", WikiCatalogLevelEnum.TWO.getValue()) // 查询二级目录
                    .isNotNull("article_id") // article_id 字段不能为空
                    .orderByAsc("id") // 按 id 增序排列
                    .last("LIMIT 1"));// 仅查询一条
            rspVO.setFirstArticleId(wikiCatalogDO.getArticleId());
            return rspVO;
        }).collect(Collectors.toList());

        return vos;

    }

    @Override
    public List<FindWikiCatalogListRspVO> catalogList(FindWikiCatalogListReqVO findWikiCatalogListReqVO) {
        List<WikiCatalogDO> wikiCatalogDOS = wikiCatalogMapper.selectList(new QueryWrapper<WikiCatalogDO>()
                .orderByAsc("level"));
        // 转换vo
        List<FindWikiCatalogListRspVO> vos = wikiCatalogDOS.stream().map(wikiCatalogDO -> {
            FindWikiCatalogListRspVO vo = new FindWikiCatalogListRspVO();
            BeanUtils.copyProperties(wikiCatalogDO, vo);
            return vo;
        }).collect(Collectors.toList());
        // 构造结构
        // 用 map 存 id -> vo 方便查找父节点
        Map<Long, FindWikiCatalogListRspVO> map = new HashMap<>();
        for (FindWikiCatalogListRspVO vo : vos) {
            map.put(vo.getId(), vo);
            vo.setChildren(new ArrayList<>()); // 先初始化 children
        }
        // 存放最终的顶级节点
        List<FindWikiCatalogListRspVO> roots = new ArrayList<>();
        for (FindWikiCatalogListRspVO vo : vos) {
            if (vo.getLevel().equals(WikiCatalogLevelEnum.ONE.getValue())) {
                // 一级目录
                roots.add(vo);
            } else {
                // 找到父节点，挂上去
                FindWikiCatalogListRspVO parent = map.get(vo.getParentId());
                if (parent != null) {
                    parent.getChildren().add(vo);
                }
            }
        }
        return roots;
    }

    @Override
    public FindWikiArticlePreNextRspVO preNext(FindWikiArticlePreNextReqVO findWikiArticlePreNextReqVO) {
        // 查询知识库是否存在
        Long id = findWikiArticlePreNextReqVO.getId();
        WikiDO wikiDO = wikiMapper.selectById(id);
        if (Objects.isNull(wikiDO)) {
            throw new BizException(ResponseCodeEnum.WIKI_NOT_EXIST);
        }
        Long articleId = findWikiArticlePreNextReqVO.getArticleId();
        // 下一篇
        WikiCatalogDO next = wikiCatalogMapper.selectOne(
                new QueryWrapper<WikiCatalogDO>()
                        .isNotNull("article_id")
                        .eq("wiki_id", id) // 同一个知识库
                        .gt("article_id", articleId) // id 要比当前文章大
                        .last("LIMIT 1")     // 只取一条
        );
        // 上一篇：比当前ID小的最大ID（降序取第一条）
        WikiCatalogDO pre = wikiCatalogMapper.selectOne(
                new QueryWrapper<WikiCatalogDO>()
                        .select("article_id", "title")
                        .isNotNull("article_id")
                        .eq("wiki_id", id)
                        .lt("article_id", articleId)
                        .last("LIMIT 1")
        );

        // 4. 处理可能为null的情况
        FindPreNextArticleRspVO preArticle = pre != null ?
                new FindPreNextArticleRspVO(pre.getArticleId(), pre.getTitle()) : null;

        FindPreNextArticleRspVO nextArticle = next != null ?
                new FindPreNextArticleRspVO(next.getArticleId(), next.getTitle()) : null;

        return new FindWikiArticlePreNextRspVO(preArticle, nextArticle);

    }
}
