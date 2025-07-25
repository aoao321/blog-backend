package com.aoao.blog.admin.service.impl;

import com.aoao.blog.admin.service.AdminArticleService;
import com.aoao.blog.common.domain.dos.*;
import com.aoao.blog.common.domain.mapper.*;
import com.aoao.blog.common.enums.ResponseCodeEnum;
import com.aoao.blog.common.exception.BizException;

import com.aoao.blog.common.model.admin.vo.article.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author aoao
 * @create 2025-07-22-16:03
 */
@Service
public class AdminArticleServiceImpl implements AdminArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleContentMapper articleContentMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleCategoryRelMapper articleCategoryRelMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagRelMapper articleTagRelMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishArticle(PublishArticleReqVO publishArticleReqVO) {

        // 存入文章大概再根据主键插入文本
        ArticleDO articleDO = new ArticleDO();
        articleDO.setTitle(publishArticleReqVO.getTitle());
        articleDO.setCover(publishArticleReqVO.getCover());
        articleDO.setSummary(publishArticleReqVO.getSummary());
        articleMapper.insert(articleDO);

        // 获得id
        Long id = articleDO.getId();
        ArticleContentDO articleContentDO = new ArticleContentDO();
        articleContentDO.setArticleId(id);
        articleContentDO.setContent(publishArticleReqVO.getContent());
        articleContentMapper.insert(articleContentDO);

        // 获取分类id
        Long categoryId = publishArticleReqVO.getCategoryId();
        CategoryDO categoryDO = categoryMapper.selectById(categoryId);
        if (Objects.isNull(categoryDO)) {
            throw new BizException(ResponseCodeEnum.CATEGORY_NOT_EXIST);
        }
        ArticleCategoryRelDO articleCategoryRelDO = new ArticleCategoryRelDO();
        articleCategoryRelDO.setArticleId(id);
        articleCategoryRelDO.setCategoryId(categoryId);
        articleCategoryRelMapper.insert(articleCategoryRelDO);

        tagOperation(id, publishArticleReqVO.getTags());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticle(DeleteArticleReqVO deleteArticleReqVO) {
        // 删除关联的表，再删除文章表
        Long articleId = deleteArticleReqVO.getId();
        ArticleDO articleDO = articleMapper.selectById(articleId);
        if (Objects.isNull(articleDO)) {
            throw new BizException(ResponseCodeEnum.ARTICLE_NOT_EXIST);
        }
        // 删除文章标签表
        articleTagRelMapper.delete(new QueryWrapper<ArticleTagRelDO>()
                .eq("article_id", articleId));
        // 删除文章类型表
        articleCategoryRelMapper.delete(new QueryWrapper<ArticleCategoryRelDO>()
                .eq("article_id", articleId));
        // 删除文章内容表
        articleContentMapper.delete(new QueryWrapper<ArticleContentDO>()
                .eq("article_id", articleId));
        // 删除文章
        articleMapper.deleteById(articleId);
    }

    @Override
    public PageInfo<FindArticlePageListRspVO> findArticlePageList(FindArticlePageListReqVO findArticlePageListReqVO) {
        // 分页
        PageHelper.startPage(findArticlePageListReqVO.getCurrent(), findArticlePageListReqVO.getSize());
        // 查询
        String title = findArticlePageListReqVO.getTitle();
        LocalDate startDate = findArticlePageListReqVO.getStartDate();
        LocalDate endDate = findArticlePageListReqVO.getEndDate();
        List<ArticleDO> articleDOS = articleMapper.selectList(new QueryWrapper<ArticleDO>()
                .like(StringUtil.isNotEmpty(title), "title", title)
                .gt(startDate != null, "create_time", startDate)
                .lt(endDate != null, "create_time", endDate));
        // 转换从vo
        List<FindArticlePageListRspVO> vos = articleDOS.stream().map(articleDO -> {
            FindArticlePageListRspVO vo = new FindArticlePageListRspVO();
            BeanUtils.copyProperties(articleDO, vo);
            return vo;
        }).collect(Collectors.toList());
        PageInfo<FindArticlePageListRspVO> pageInfo = new PageInfo<>(vos);
        return pageInfo;
    }

    @Override
    public FindArticleDetailRspVO showDetail(FindArticleDetailReqVO findArticleDetailReqVO) {
        Long articleId = findArticleDetailReqVO.getId();
        // 查询vo
        FindArticleDetailRspVO rspVO = articleMapper.selectDetail(articleId);
        List<Long> tags = tagMapper.selectByArticleId(articleId);
        if (rspVO != null) {
            rspVO.setTagIds(tags);
        } else {
            throw new BizException(ResponseCodeEnum.ARTICLE_NOT_EXIST);
        }
        return rspVO;
    }

    @Override
    @Transactional
    public void updateArticle(UpdateArticleReqVO updateArticleReqVO) {
        // 更新文章表
        Long id = updateArticleReqVO.getId();
        ArticleDO article = articleMapper.selectById(id);
        if (article!=null) {
            ArticleDO articleDO = new ArticleDO();
            BeanUtils.copyProperties(updateArticleReqVO, articleDO);
            articleMapper.updateById(articleDO);
            // 文章内容表
            Long articleId = articleDO.getId();
            articleContentMapper.update(null, new UpdateWrapper<ArticleContentDO>()
                    .set("content", updateArticleReqVO.getContent())
                    .eq("article_id", articleId));
            //  更新分类信息
            Long newCategoryId = updateArticleReqVO.getCategoryId();
            CategoryDO categoryDO = categoryMapper.selectById(newCategoryId);
            if (categoryDO != null) {
                // 删除旧的分类关联
                articleCategoryRelMapper.delete(new QueryWrapper<ArticleCategoryRelDO>()
                        .eq("article_id", articleId));

                // 添加新的分类关联
                ArticleCategoryRelDO newRel = new ArticleCategoryRelDO();
                newRel.setArticleId(articleId);
                newRel.setCategoryId(newCategoryId);
                articleCategoryRelMapper.insert(newRel);
            } else {
                throw new BizException(ResponseCodeEnum.CATEGORY_NOT_EXIST);
            }
            // 先删除标签记录
            articleTagRelMapper.delete(new QueryWrapper<ArticleTagRelDO>()
                    .eq("article_id", articleId));
            // 插入新标签记录
            List<String> publishTags = updateArticleReqVO.getTags();
            tagOperation(articleId, publishTags);
        }
        else {
            throw new BizException(ResponseCodeEnum.ARTICLE_NOT_EXIST);
        }

    }


    private void tagOperation(Long articleID, List<String> tags) {
        // 从数据库查询出已经存在的标签
        List<TagDO> existList = Optional.ofNullable(tagMapper.selectExist(tags)).orElse(Collections.emptyList());
        // 建立与已存在标签的关联
        if (!existList.isEmpty()) {
            List<ArticleTagRelDO> articleTagRelDOList = existList.stream().map(tag -> {
                ArticleTagRelDO rel = new ArticleTagRelDO();
                rel.setArticleId(articleID);
                rel.setTagId(tag.getId());
                return rel;
            }).collect(Collectors.toList());

            articleTagRelMapper.insertBatch(articleTagRelDOList);
        }

        // 继续筛选出不存在的标签
        Set<String> tagSet = new HashSet<>(tags);
        List<String> collect = existList
                .stream()
                .map(TagDO::getName)
                .collect(Collectors.toList());
        // 移除已存在的标签，得到真正不存在的标签集合
        tagSet.removeAll(collect);

        // 去掉存在的标签不为空，就插入数据库中没有的标签了
        if (!tagSet.isEmpty()) {
            // 转换为 TagDO 列表再批量插入
            List<TagDO> newTagList = tagSet.stream()
                    .map(tagName -> {
                        TagDO tag = new TagDO();
                        tag.setName(tagName);
                        return tag;
                    }).collect(Collectors.toList());
            tagMapper.insertBatch(newTagList);

            // 插入后查询新插入的标签（为了拿到 ID）
            List<TagDO> insertedTags = tagMapper.selectExist(new ArrayList<>(tagSet));

            // 建立关联
            List<ArticleTagRelDO> newTagRelList = new ArrayList<>();
            for (TagDO tag : insertedTags) {
                ArticleTagRelDO rel = new ArticleTagRelDO();
                rel.setArticleId(articleID);
                rel.setTagId(tag.getId());
                newTagRelList.add(rel);
            }
            articleTagRelMapper.insertBatch(newTagRelList);
        }


    }
}
