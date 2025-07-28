package com.aoao.blog.web.service.impl;

import com.aoao.blog.common.domain.dos.ArticleDO;
import com.aoao.blog.common.domain.dos.CategoryDO;
import com.aoao.blog.common.domain.mapper.ArticleMapper;
import com.aoao.blog.common.domain.mapper.CategoryMapper;
import com.aoao.blog.common.model.front.vo.category.FindArticleWithTypeCategoryVO;
import com.aoao.blog.common.model.front.vo.category.FindCategoryArticlePageListRspVO;
import com.aoao.blog.common.model.front.vo.category.FindCategoryListRspVO;
import com.aoao.blog.web.service.CategoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author aoao
 * @create 2025-07-25-12:29
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<FindCategoryListRspVO> list() {

        List<CategoryDO> tags = categoryMapper.selectList(null);
        List<FindCategoryListRspVO> vos = tags.stream().map(tag -> {
            FindCategoryListRspVO findCategoryListRspVO = new FindCategoryListRspVO();
            BeanUtils.copyProperties(tag, findCategoryListRspVO);
            return findCategoryListRspVO;
        }).collect(Collectors.toList());
        // 统计标签下的文章数量 {id=7,count(*)=2}
        List<Map<String,Object>> categoryMapList = categoryMapper.selectWithArticleTotal();
        HashMap<Long, Long> articleTotalMap = new HashMap<>();
        for (Map<String, Object> map : categoryMapList) {
            Long id = ((Number) map.get("id")).longValue();
            Long articleTotal = ((Long) map.get("articleTotal")).longValue();
            articleTotalMap.put(id, articleTotal);
        }
        // 合并文章数量到 VO
        for (FindCategoryListRspVO vo : vos) {
            Long total = articleTotalMap.getOrDefault(vo.getId(), 0L);
            vo.setArticlesTotal(total);
        }
        return vos;
    }

    @Override
    public PageInfo<FindCategoryArticlePageListRspVO> findCategoryArticlePageList(FindArticleWithTypeCategoryVO reqVO) {
        PageHelper.startPage(reqVO.getCurrent(), reqVO.getSize());
        Long typeId = reqVO.getId();
        // 查询
        List<ArticleDO> articleDOList = articleMapper.selectByCategory(typeId);
        List<FindCategoryArticlePageListRspVO> vos = articleDOList.stream().map(articleDO -> {
            FindCategoryArticlePageListRspVO vo = new FindCategoryArticlePageListRspVO();
            BeanUtils.copyProperties(articleDO, vo);
            vo.setCreateDate(articleDO.getCreateTime().toLocalDate());
            return vo;
        }).collect(Collectors.toList());
        PageInfo<FindCategoryArticlePageListRspVO> pageInfo = new PageInfo<>(vos);
        return pageInfo;
    }
}
