package com.aoao.blog.admin.service;

import com.aoao.blog.common.model.admin.vo.notice.*;
import com.github.pagehelper.PageInfo;

/**
 * @author aoao
 * @create 2025-07-30-16:20
 */
public interface AdminNoticeService {
    void add(AddNoticeReqVO addNoticeReqVO);

    void update(UpdateNoticeReqVO updateNoticeReqVO);

    void delete(DeleteNoticeReqVO deleteNoticeReqVO);

    PageInfo<FindNoticePageListRspVO> list(FindNoticePageListReqVO findNoticePageListReqVO);
}
