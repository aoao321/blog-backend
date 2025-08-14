package com.aoao.blog.admin.service.impl;

import com.aoao.blog.admin.service.AdminWikiService;
import com.aoao.blog.common.domain.dos.WikiCatalogDO;
import com.aoao.blog.common.domain.dos.WikiDO;
import com.aoao.blog.common.domain.dos.ArticleDO;
import com.aoao.blog.common.domain.mapper.ArticleMapper;
import com.aoao.blog.common.domain.mapper.WikiCatalogMapper;
import com.aoao.blog.common.domain.mapper.WikiMapper;
import com.aoao.blog.common.enums.ArticleTypeEnum;
import com.aoao.blog.common.enums.ResponseCodeEnum;
import com.aoao.blog.common.enums.WikiCatalogLevelEnum;
import com.aoao.blog.common.exception.BizException;
import com.aoao.blog.common.model.admin.vo.wiki.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author aoao
 * @create 2025-08-08-13:42
 */
@Service
public class AdminWikiServiceImpl implements AdminWikiService {

    @Autowired
    private WikiMapper wikiMapper;
    @Autowired
    private WikiCatalogMapper wikiCatalogMapper;
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public void add(AddWikiReqVO addWikiReqVO) {
        WikiDO wikiDO = new WikiDO();
        BeanUtils.copyProperties(addWikiReqVO, wikiDO);
        // 新增知识库
        wikiMapper.insert(wikiDO);
        // 获取新增记录的主键 ID
        Long wikiId = wikiDO.getId();

        // 初始化默认目录
        // > 概述
        // > 基础
        wikiCatalogMapper.insert(WikiCatalogDO.builder().wikiId(wikiId).title("概述").sort(1).build());
        wikiCatalogMapper.insert(WikiCatalogDO.builder().wikiId(wikiId).title("基础").sort(2).build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(DeleteWikiReqVO deleteWikiReqVO) {
        // 获取删除的知识库id
        Long id = deleteWikiReqVO.getId();
        WikiDO wikiDO = wikiMapper.selectById(id);
        if (Objects.isNull(wikiDO)) {
            throw new BizException(ResponseCodeEnum.WIKI_NOT_EXIST);
        }
        // 删除知识库
        wikiMapper.deleteById(id);
        // 查询所有目录
        List<WikiCatalogDO> wikiCatalogDOS = wikiCatalogMapper
                .selectList(new QueryWrapper<WikiCatalogDO>()
                        .eq("wiki_id", id));
        // 遍历出所有文章id
        List<Long> articleIds = wikiCatalogDOS.stream()
                .filter(wikiCatalogDO -> Objects.nonNull(wikiCatalogDO.getArticleId())
                        && Objects.equals(wikiCatalogDO.getLevel(), WikiCatalogLevelEnum.TWO.getValue())) // 二级目录
                .map(wikiCatalogDO -> wikiCatalogDO.getArticleId())
                .collect(Collectors.toList());
        // 将文章的type修改为1
        if (articleIds!=null && articleIds.size()>0) {
            articleMapper.updateType(articleIds, ArticleTypeEnum.NORMAL.getValue());
        }
        // 删除目录
        List<Long> wikiCatalogIds = wikiCatalogDOS.stream()
                .map(WikiCatalogDO::getId)
                .collect(Collectors.toList());
        wikiCatalogMapper.deleteBatchIds(wikiCatalogIds);


    }

    @Override
    public PageInfo<FindWikiPageListRspVO> page(FindWikiPageListReqVO findWikiPageListReqVO) {
        LocalDate endDate = findWikiPageListReqVO.getEndDate();
        String title = findWikiPageListReqVO.getTitle();
        LocalDate startDate = findWikiPageListReqVO.getStartDate();
        // 分页
        PageHelper.startPage(findWikiPageListReqVO.getCurrent(), findWikiPageListReqVO.getSize());
        // 查询
        List<WikiDO> wikiDOS = wikiMapper.selectList(new QueryWrapper<WikiDO>()
                .like(StringUtil.isNotEmpty(title), "title", title)
                .gt(startDate != null, "create_time", startDate)
                .lt(startDate != null, "create_time", endDate)
                .orderByDesc("weight"));
        // 封装vo
        List<FindWikiPageListRspVO> vos = wikiDOS.stream().map(wikiDO -> {
            FindWikiPageListRspVO findWikiPageListRspVO = new FindWikiPageListRspVO();
            findWikiPageListRspVO.setIsTop(false);
            BeanUtils.copyProperties(wikiDO, findWikiPageListRspVO);
            if (wikiDO.getWeight()>0){
                findWikiPageListRspVO.setIsTop(true);
            }
            return findWikiPageListRspVO;
        }).collect(Collectors.toList());
        PageInfo<FindWikiPageListRspVO> pageInfo = new PageInfo<>(vos);
        return pageInfo;
    }

    @Override
    public void top(UpdateWikiIsTopReqVO updateWikiIsTopReqVO) {
        Long wikiId = updateWikiIsTopReqVO.getId();
        Boolean isTop = updateWikiIsTopReqVO.getIsTop();

        // 默认权重值为 0 ，即不参与置顶
        Integer weight = 0;
        // 若设置为置顶
        if (Boolean.TRUE.equals(isTop)) {
            // 查询最大权重值
            Integer maxWeight = wikiMapper.selectMaxWeight();
            // 最大权重值加一
            weight = maxWeight + 1;
        }

        // 更新该知识库的权重值
        wikiMapper.updateById(WikiDO.builder().id(wikiId).weight(weight).build());
    }

    @Override
    public void publish(UpdateWikiIsPublishReqVO updateWikiIsPublishReqVO) {
        // 查询
        Long wikiId = updateWikiIsPublishReqVO.getId();
        WikiDO wikiDO = wikiMapper.selectById(wikiId);
        if (Objects.isNull(wikiDO)) {
            throw new BizException(ResponseCodeEnum.WIKI_NOT_EXIST);
        }
        Boolean isPublish = updateWikiIsPublishReqVO.getIsPublish();
        // 更新发布状态
        wikiMapper.updateById(WikiDO.builder().id(wikiId).isPublish(isPublish).build());
    }

    @Override
    public void update(UpdateWikiReqVO updateWikiReqVO) {
        WikiDO wikiDO = new WikiDO();
        BeanUtils.copyProperties(updateWikiReqVO, wikiDO);
        wikiMapper.updateById(wikiDO);
    }

    @Override
    public List<FindWikiCatalogListRspVO> findCatalogList(FindWikiCatalogListReqVO findWikiCatalogListReqVO) {
        Long wikiId = findWikiCatalogListReqVO.getId();
        // 查询知识库是否存在
        WikiDO wikiDO = wikiMapper.selectById(wikiId);
        if (Objects.isNull(wikiDO)) {
            throw new BizException(ResponseCodeEnum.WIKI_NOT_EXIST);
        }
        // 查询目录集合
        List<WikiCatalogDO> wikiCatalogDOS = wikiCatalogMapper.selectList(new QueryWrapper<WikiCatalogDO>()
                .eq("wiki_id", wikiId));
        // 转vo
        List<FindWikiCatalogListRspVO> vos = wikiCatalogDOS.stream().map(wikiCatalogDO -> {
            FindWikiCatalogListRspVO vo = new FindWikiCatalogListRspVO();
            BeanUtils.copyProperties(wikiCatalogDO, vo);
            vo.setEditing(false);
            return vo;
        }).collect(Collectors.toList());
        // 建立树状结构
        buildTree(vos);
        return vos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCatalog(UpdateWikiCatalogReqVO updateWikiCatalogReqVO) {
        // 知识库 ID
        Long wikiId = updateWikiCatalogReqVO.getId();
        // 查询
        WikiDO wikiDO = wikiMapper.selectById(wikiId);
        if (Objects.isNull(wikiDO)) {
            throw new BizException(ResponseCodeEnum.WIKI_NOT_EXIST);
        }
        // 目录
        List<UpdateWikiCatalogItemReqVO> catalogs = updateWikiCatalogReqVO.getCatalogs();

        // 删除原本目录
        wikiCatalogMapper.delete(new QueryWrapper<WikiCatalogDO>().eq("wiki_id",wikiId));
        // 再重新插入新的目录数据
        // 若入参传入的目录不为空
        if (!CollectionUtils.isEmpty(catalogs)) {
            // 重新设置 sort 排序字段的值
            for (int i = 0; i < catalogs.size(); i++) {
                UpdateWikiCatalogItemReqVO vo = catalogs.get(i);
                List<UpdateWikiCatalogItemReqVO> children = vo.getChildren();
                vo.setSort(i + 1);
                if (!CollectionUtils.isEmpty(children)) {
                    for (int j = 0; j < children.size(); j++) {
                        children.get(j).setSort(j + 1);
                    }
                }
            }

            // VO 转 DO
            catalogs.forEach(catalog -> {
                // 一级目录
                WikiCatalogDO wikiCatalogDO = WikiCatalogDO.builder()
                        .wikiId(wikiId)
                        .title(catalog.getTitle())
                        .level(WikiCatalogLevelEnum.ONE.getValue())
                        .sort(catalog.getSort())
                        .build();
                // 添加一级目录
                wikiCatalogMapper.insert(wikiCatalogDO);

                // 一级目录 ID
                Long catalogId = wikiCatalogDO.getId();

                // 获取下面的二级目录
                List<UpdateWikiCatalogItemReqVO> children = catalog.getChildren();
                // 需要被更新 type 字段的所有文章 ID
                List<Long> updateArticleIds = Lists.newArrayList();
                if (!CollectionUtils.isEmpty(children)) {
                    List<WikiCatalogDO> level2Catalogs = Lists.newArrayList();
                    // VO 转 DO
                    children.forEach(child -> {
                        level2Catalogs.add(WikiCatalogDO.builder()
                                .wikiId(wikiId)
                                .title(child.getTitle())
                                .level(WikiCatalogLevelEnum.TWO.getValue())
                                .sort(child.getSort())
                                .articleId(child.getArticleId())
                                .parentId(catalogId)
                                .createTime(LocalDateTime.now())
                                .updateTime(LocalDateTime.now())
                                .isDeleted(Boolean.FALSE)
                                .build());

                        updateArticleIds.add(child.getArticleId());
                    });

                    // 批量插入二级目录数据
                    for (WikiCatalogDO level2Catalog : level2Catalogs) {
                        wikiCatalogMapper.insert(level2Catalog);
                    }
                    // 更新相关文章的 type 字段，知识库类型
                    articleMapper.updateType(updateArticleIds, ArticleTypeEnum.WIKI.getValue());
                }
            });
        }


    }

    private List<FindWikiCatalogListRspVO> buildTree(List<FindWikiCatalogListRspVO> vos) {
        // id --> node 映射
        Map<Long, FindWikiCatalogListRspVO> idNodeMap = new HashMap<>();
        for (FindWikiCatalogListRspVO item : vos) {
            idNodeMap.put(item.getId(), item);
            item.setChildren(new ArrayList<>()); // 初始化 children
        }
        List<FindWikiCatalogListRspVO> rootNodes = new ArrayList<>();
        for (FindWikiCatalogListRspVO item : vos) {
            Long parentId = item.getParentId();
            if (parentId == null || parentId == 0) {
                // 顶级目录
                rootNodes.add(item);
            } else {
                FindWikiCatalogListRspVO parent = idNodeMap.get(parentId);
                if (parent != null) {
                    parent.getChildren().add(item);
                }
            }
        }
        return rootNodes;
    }


}
