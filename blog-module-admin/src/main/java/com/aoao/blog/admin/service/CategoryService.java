package com.aoao.blog.admin.service;

import com.aoao.blog.common.model.admin.vo.category.*;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author aoao
 * @create 2025-07-21-19:13
 */
public interface CategoryService {
    void addCategory(AddCategoryReqVO addCategoryReqVO);

    PageInfo<FindCategoryPageListRspVO> page(FindCategoryPageListReqVO findCategoryPageListReqVO);

    void deleteCategory( DeleteCategoryReqVO deleteCategoryReqVO);

    List<SelectCategoryListRspVO> selectList();

}
