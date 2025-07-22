package com.aoao.blog.admin.service;

import com.aoao.blog.admin.model.vo.category.AddCategoryReqVO;
import com.aoao.blog.admin.model.vo.category.DeleteCategoryReqVO;
import com.aoao.blog.admin.model.vo.category.FindCategoryPageListReqVO;
import com.aoao.blog.admin.model.vo.category.FindCategoryPageListRspVO;
import com.aoao.blog.common.utils.PageResult;

import javax.validation.Valid;

/**
 * @author aoao
 * @create 2025-07-21-19:13
 */
public interface CategoryService {
    void addCategory(AddCategoryReqVO addCategoryReqVO);

    PageResult<FindCategoryPageListRspVO> page(FindCategoryPageListReqVO findCategoryPageListReqVO);

    void deleteCategory( DeleteCategoryReqVO deleteCategoryReqVO);
}
