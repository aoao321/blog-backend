package com.aoao.blog.admin.service.impl;

import com.aoao.blog.common.domain.dos.ArticleCategoryRelDO;
import com.aoao.blog.common.domain.mapper.ArticleCategoryRelMapper;
import com.aoao.blog.common.model.admin.vo.category.*;
import com.aoao.blog.admin.service.AdminCategoryService;
import com.aoao.blog.common.domain.dos.CategoryDO;
import com.aoao.blog.common.domain.mapper.CategoryMapper;
import com.aoao.blog.common.enums.ResponseCodeEnum;
import com.aoao.blog.common.exception.BizException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author aoao
 * @create 2025-07-21-19:13
 */
@Service
@Slf4j
public class AdminCategoryServiceImpl implements AdminCategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleCategoryRelMapper articleCategoryRelMapper;

    @Override
    public void addCategory(AddCategoryReqVO addCategoryReqVO) {
        // 查询数据库中是否存在有该目录，如果有则返回信息
        String categoryReqVOName = addCategoryReqVO.getName();
        CategoryDO categoryDO = categoryMapper.selectOne(new QueryWrapper<CategoryDO>()
                .eq("name", categoryReqVOName));
        if (categoryDO == null) {// 说明没有
            CategoryDO newCategory = new CategoryDO();
            newCategory.setName(categoryReqVOName);
            categoryMapper.insert(newCategory);
        }else {
            throw new BizException(ResponseCodeEnum.CATEGORY_EXIST);
        }
    }

    @Override
    public PageInfo<FindCategoryPageListRspVO> page(FindCategoryPageListReqVO findCategoryPageListReqVO) {
        PageHelper.startPage(findCategoryPageListReqVO.getCurrent(),
                findCategoryPageListReqVO.getSize());
        // 根据传入的条件查询
        String name = findCategoryPageListReqVO.getName();
        LocalDate startDate = findCategoryPageListReqVO.getStartDate();
        LocalDate endDate = findCategoryPageListReqVO.getEndDate();
        List<CategoryDO> categoryDOS = categoryMapper.selectList(new QueryWrapper<CategoryDO>()
                .like(StringUtil.isNotEmpty(name),"name",name)
                .gt(startDate!=null,"create_time",startDate)
                .lt(endDate!=null,"create_time",endDate));
        // 封装成FindCategoryPageListRspVO集合
        List<FindCategoryPageListRspVO> vos = categoryDOS.stream()
                .map(categoryDO -> {
                    FindCategoryPageListRspVO vo = new FindCategoryPageListRspVO();
                    BeanUtils.copyProperties(categoryDO, vo);
                    return vo;
                })
                .collect(Collectors.toList());
        PageInfo<FindCategoryPageListRspVO> pageInfo = new PageInfo<>(vos);
        return pageInfo;
    }

    @Override
    public void deleteCategory(DeleteCategoryReqVO deleteCategoryReqVO) {
        Long categoryId = deleteCategoryReqVO.getId();
        CategoryDO categoryDO = categoryMapper.selectById(categoryId);
        if (categoryDO != null) {
            // 校验该分类下是否已经有文章，若有，则提示需要先删除分类下所有文章，才能删除
            ArticleCategoryRelDO articleCategoryRelDO = articleCategoryRelMapper.selectOne(new QueryWrapper<ArticleCategoryRelDO>()
                    .eq("category_id",categoryId));
            if (Objects.nonNull(articleCategoryRelDO)) {
                log.warn("==> 此分类下包含文章，无法删除，categoryId: {}", categoryId);
                throw new BizException(ResponseCodeEnum.CATEGORY_CAN_NOT_DELETE);
            }
            categoryMapper.deleteById(categoryId);
        }else {
            throw new BizException(ResponseCodeEnum.CATEGORY_NOT_EXIST);
        }
    }

    @Override
    public List<SelectCategoryListRspVO> selectList() {
        // 查询所有category
        List<CategoryDO> categoryDOS = categoryMapper.selectList(null);
        // 转换成vo
        List<SelectCategoryListRspVO> vos = new ArrayList<>();
        for (CategoryDO categoryDO : categoryDOS) {
            SelectCategoryListRspVO selectCategoryListRspVO = new SelectCategoryListRspVO();
            selectCategoryListRspVO.setLabel(categoryDO.getName());
            selectCategoryListRspVO.setValue(categoryDO.getId());
            vos.add(selectCategoryListRspVO);
        }
        return vos;
    }


}
