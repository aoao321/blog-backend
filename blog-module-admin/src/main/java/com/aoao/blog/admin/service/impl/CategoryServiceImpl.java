package com.aoao.blog.admin.service.impl;

import com.aoao.blog.admin.model.vo.category.AddCategoryReqVO;
import com.aoao.blog.admin.model.vo.category.DeleteCategoryReqVO;
import com.aoao.blog.admin.model.vo.category.FindCategoryPageListReqVO;
import com.aoao.blog.admin.model.vo.category.FindCategoryPageListRspVO;
import com.aoao.blog.admin.service.CategoryService;
import com.aoao.blog.common.domain.dos.CategoryDO;
import com.aoao.blog.common.domain.mapper.CategoryMapper;
import com.aoao.blog.common.enums.ResponseCodeEnum;
import com.aoao.blog.common.exception.BizException;
import com.aoao.blog.common.utils.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author aoao
 * @create 2025-07-21-19:13
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

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
    public PageResult<FindCategoryPageListRspVO> page(FindCategoryPageListReqVO findCategoryPageListReqVO) {
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
        return PageResult.success(pageInfo);
    }

    @Override
    public void deleteCategory(DeleteCategoryReqVO deleteCategoryReqVO) {
        Long categoryId = deleteCategoryReqVO.getId();
        CategoryDO categoryDO = categoryMapper.selectById(categoryId);
        if (categoryDO == null) {
            throw new BizException(ResponseCodeEnum.CATEGORY_NOT_EXIST);
        }
    }


}
