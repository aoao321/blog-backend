package com.aoao.blog.admin.service;

import com.aoao.blog.admin.model.vo.tag.AddTagReqVO;
import com.aoao.blog.admin.model.vo.tag.DeleteTagReqVO;
import com.aoao.blog.admin.model.vo.tag.FindTagPageListReqVO;
import com.aoao.blog.admin.model.vo.tag.FindTagPageListRspVO;
import com.aoao.blog.common.utils.PageResult;

import javax.validation.Valid;

/**
 * @author aoao
 * @create 2025-07-22-9:36
 */
public interface TagService {
    void addTag(@Valid AddTagReqVO addTagReqVO);

    void deleteTag(@Valid DeleteTagReqVO deleteTagReqVO);

    PageResult<FindTagPageListRspVO> page(@Valid FindTagPageListReqVO findTagPageListReqVO);
}
