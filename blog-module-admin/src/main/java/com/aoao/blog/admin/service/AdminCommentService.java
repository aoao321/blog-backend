package com.aoao.blog.admin.service;

import com.aoao.blog.common.model.admin.vo.comment.DeleteCommentReqVO;
import com.aoao.blog.common.model.admin.vo.comment.ExamineCommentReqVO;
import com.aoao.blog.common.model.admin.vo.comment.FindCommentPageListReqVO;
import com.aoao.blog.common.model.admin.vo.comment.FindCommentPageListRspVO;
import com.github.pagehelper.PageInfo;

/**
 * @author aoao
 * @create 2025-08-11-17:01
 */
public interface AdminCommentService {
    PageInfo<FindCommentPageListRspVO> page(FindCommentPageListReqVO findCommentPageListReqVO);

    void delete(DeleteCommentReqVO deleteCommentReqVO);

    void examine(ExamineCommentReqVO examineCommentReqVO);
}
