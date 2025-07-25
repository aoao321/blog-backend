package com.aoao.blog.admin.service;

import com.aoao.blog.common.model.admin.vo.tag.*;
import com.github.pagehelper.PageInfo;

import javax.validation.Valid;
import java.util.List;

/**
 * @author aoao
 * @create 2025-07-22-9:36
 */
public interface AdminTagService {
    void addTag(@Valid AddTagReqVO addTagReqVO);

    void deleteTag(@Valid DeleteTagReqVO deleteTagReqVO);

    PageInfo<FindTagPageListRspVO> page(@Valid FindTagPageListReqVO findTagPageListReqVO);

    List<SelectTagListRspVO> selectTagList();
}
