package com.aoao.blog.web.service.impl;

import com.aoao.blog.common.domain.dos.CategoryDO;
import com.aoao.blog.common.domain.dos.TagDO;
import com.aoao.blog.common.domain.mapper.CategoryMapper;
import com.aoao.blog.common.model.front.vo.category.FindCategoryListRspVO;
import com.aoao.blog.common.model.front.vo.tag.FindTagListRspVO;
import com.aoao.blog.web.service.CategoryService;
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
 * @create 2025-07-25-12:29
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

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
}
