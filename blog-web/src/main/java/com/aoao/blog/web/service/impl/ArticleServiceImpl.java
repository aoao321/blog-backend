package com.aoao.blog.web.service.impl;

import com.aoao.blog.common.domain.dos.ArticleDO;
import com.aoao.blog.common.domain.mapper.ArticleCategoryRelMapper;
import com.aoao.blog.common.domain.mapper.ArticleMapper;
import com.aoao.blog.common.domain.mapper.CategoryMapper;
import com.aoao.blog.common.domain.mapper.TagMapper;
import com.aoao.blog.common.model.BasePageQuery;
import com.aoao.blog.common.model.front.vo.article.FindIndexArticlePageListRspVO;
import com.aoao.blog.common.model.front.vo.category.FindCategoryListRspVO;
import com.aoao.blog.common.model.front.vo.tag.FindTagListRspVO;
import com.aoao.blog.web.service.ArticleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author aoao
 * @create 2025-07-24-14:03
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleCategoryRelMapper articleCategoryRelMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;

    @Override
    public PageInfo<FindIndexArticlePageListRspVO> page(BasePageQuery query) {
        // 开启分页
        PageHelper.startPage(query.getCurrent(), query.getSize());
        List<ArticleDO> articleList = articleMapper.selectList(new QueryWrapper<ArticleDO>()
                .orderByDesc("create_time"));

        if (articleList == null || articleList.isEmpty()) {
            return new PageInfo<>(Collections.emptyList());
        }

        List<FindIndexArticlePageListRspVO> voList = buildArticleVOListWithCategoryAndTag(articleList);

        // 构建分页信息，注意这里用 voList 构造 PageInfo
        PageInfo<FindIndexArticlePageListRspVO> pageInfo = new PageInfo<>(voList);
        return pageInfo;
    }

    private List<FindIndexArticlePageListRspVO> buildArticleVOListWithCategoryAndTag(List<ArticleDO> articleList) {
        // 把 ArticleDO 转成 VO
        List<FindIndexArticlePageListRspVO> voList = articleList.stream()
                .map(article -> {
                    FindIndexArticlePageListRspVO vo = new FindIndexArticlePageListRspVO();
                    BeanUtils.copyProperties(article, vo);
                    return vo;
                }).collect(Collectors.toList());

        // 获取ids
        List<Long> articleIds = voList.stream()
                .map(vo -> vo.getId())
                .collect(Collectors.toList());

        // 根据id获取到分类
        // {name=java, articleId=3, id=2}
        List<Map<String, Object>> categoryList = categoryMapper.selectBatchByArticleIds(articleIds);
        // 将分类按 articleId 分组
        Map<Long, FindCategoryListRspVO> articleCategoryMap = new HashMap<>();
        for (Map<String, Object> map : categoryList) {
            Long articleId = ((Number) map.get("articleId")).longValue();
            FindCategoryListRspVO findCategoryListRspVO = new FindCategoryListRspVO();
            findCategoryListRspVO.setId(((Number) map.get("id")).longValue());
            findCategoryListRspVO.setName((String) map.get("name"));
            findCategoryListRspVO.setArticlesTotal(0l);
            // 放入map中
            articleCategoryMap.put(articleId, findCategoryListRspVO);
        }
        // 从map根据articleId获取到FindCategoryListRspVO填充分类
        for (FindIndexArticlePageListRspVO vo : voList) {
            vo.setCategory(articleCategoryMap.get(vo.getId()));
        }

        // 获取标签集合
        List<Map<String, Object>> tagMapList = tagMapper.selectBatchByArticleIds(articleIds);
        // 分类
        Map<Long, List<FindTagListRspVO>> tagMap = new HashMap<>();
        for (Map<String, Object> map : tagMapList) {
            Long articleId = ((Number) map.get("articleId")).longValue();
            Long tagId = ((Number) map.get("id")).longValue();
            String name = (String) map.get("name");

            FindTagListRspVO tag = new FindTagListRspVO(tagId, name,0l);
            tagMap.computeIfAbsent(articleId, k -> new ArrayList<>()).add(tag);
        }
        // 填充
        for (FindIndexArticlePageListRspVO vo : voList) {
            vo.setTags(tagMap.get(vo.getId()));
        }

        return voList;
    }

}
