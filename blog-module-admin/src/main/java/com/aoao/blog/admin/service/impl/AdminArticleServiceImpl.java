package com.aoao.blog.admin.service.impl;

import com.aoao.blog.admin.service.AdminArticleService;
import com.aoao.blog.common.constant.RedisConstant;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.parameters.P;
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
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishArticle(PublishArticleReqVO publishArticleReqVO) {
        // 删除redis中数据
        Set<String> keys = stringRedisTemplate.keys(RedisConstant.CACHE_ARTICLE_PAGE_KEY+"*");
        if (keys != null && !keys.isEmpty()) {
            stringRedisTemplate.delete(keys);
        }

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
        // 删除redis中数据
        Set<String> keys = stringRedisTemplate.keys(RedisConstant.CACHE_ARTICLE_PAGE_KEY+"*");
        if (keys != null && !keys.isEmpty()) {
            stringRedisTemplate.delete(keys);
        }
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
            if(articleDO.getWeight()!=null && articleDO.getWeight()>0){
                vo.setIsTop(true);
            }
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
        // 删除redis中数据
        Set<String> keys = stringRedisTemplate.keys(RedisConstant.CACHE_ARTICLE_PAGE_KEY+"*");
        if (keys != null && !keys.isEmpty()) {
            stringRedisTemplate.delete(keys);
        }
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
            // 插入新标签记录，如果存在标签传入的是id，不存在传入的是string
            List<String> publishTags = updateArticleReqVO.getTags();
            tagOperation(articleId, publishTags);
        }
        else {
            throw new BizException(ResponseCodeEnum.ARTICLE_NOT_EXIST);
        }

    }

    @Override
    public void isTopUpdate(IsTopUpdateArticleReqVO isTopUpdateArticleReqVO) {
        Long id = isTopUpdateArticleReqVO.getId();
        ArticleDO articleDO = articleMapper.selectById(id);
        if (Objects.isNull(articleDO)) {
            throw new BizException(ResponseCodeEnum.ARTICLE_NOT_EXIST);
        }
        Integer maxWeight = articleMapper.selectMaxWeight();
        if (isTopUpdateArticleReqVO.getIsTop() == false) {
            maxWeight = 0;
        }else {
            // 设置新 weight 为最大值 +1（确保置顶）
            articleDO.setWeight(maxWeight + 1);
        }
        articleMapper.updateById(articleDO);
    }

    @Override
    public void isPublishUpdate(IsPublishUpdateArticleReqVO isPublishUpdateArticleReqVO) {
        Long id = isPublishUpdateArticleReqVO.getId();
        ArticleDO articleDO = articleMapper.selectById(id);
        if (Objects.isNull(articleDO)) {
            throw new BizException(ResponseCodeEnum.ARTICLE_NOT_EXIST);
        }
        articleDO.setIsPublish(isPublishUpdateArticleReqVO.getIsPublish());
        articleMapper.updateById(articleDO);
    }


    private void tagOperation(Long articleId, List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return;
        }

        // 分离数字ID和字符串标签名
        List<Long> tagIds = new ArrayList<>();
        List<String> newTagNames = new ArrayList<>();

        for (String tag : tags) {
            if (tag.matches("\\d+")) { // 判断是否为数字ID
                tagIds.add(Long.parseLong(tag));
            } else {
                newTagNames.add(tag);
            }
        }

        // 处理已存在的标签(通过ID传入的)
        if (!tagIds.isEmpty()) {
            List<TagDO> existTags = tagMapper.selectByIds(tagIds);
            if (!existTags.isEmpty()) {
                List<ArticleTagRelDO> relations = existTags.stream()
                        .map(tag -> {
                            ArticleTagRelDO rel = new ArticleTagRelDO();
                            rel.setArticleId(articleId);
                            rel.setTagId(tag.getId());
                            return rel;
                        })
                        .collect(Collectors.toList());
                articleTagRelMapper.insertBatch(relations);
            }
        }

        // 处理新标签(通过字符串传入的)
        if (!newTagNames.isEmpty()) {
            // 先查询哪些标签名已经存在(防止重复)
            List<TagDO> alreadyExistTags = tagMapper.selectByNames(newTagNames);
            Set<String> existingNames = alreadyExistTags.stream()
                    .map(TagDO::getName)
                    .collect(Collectors.toSet());

            // 过滤出真正需要新增的标签
            List<TagDO> tagsToInsert = newTagNames.stream()
                    .filter(name -> !existingNames.contains(name))
                    .map(name -> {
                        TagDO tag = new TagDO();
                        tag.setName(name);
                        return tag;
                    })
                    .collect(Collectors.toList());

            // 批量插入新标签
            if (!tagsToInsert.isEmpty()) {
                tagMapper.insertBatch(tagsToInsert);
            }

            // 获取所有需要关联的标签(包括刚插入的和已经存在的)
            List<TagDO> allTagsToRelate = new ArrayList<>();
            allTagsToRelate.addAll(alreadyExistTags);
            allTagsToRelate.addAll(tagsToInsert);

            // 建立关联关系
            List<ArticleTagRelDO> newRelations = allTagsToRelate.stream()
                    .map(tag -> {
                        ArticleTagRelDO rel = new ArticleTagRelDO();
                        rel.setArticleId(articleId);
                        rel.setTagId(tag.getId());
                        return rel;
                    })
                    .collect(Collectors.toList());

            articleTagRelMapper.insertBatch(newRelations);
        }
    }
}
